package org.alexdev.havana.server.rcon;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.alexdev.havana.Havana;
import org.alexdev.havana.dao.mysql.*;
import org.alexdev.havana.game.GameScheduler;
import org.alexdev.havana.game.achievements.AchievementManager;
import org.alexdev.havana.game.achievements.AchievementType;
import org.alexdev.havana.game.ads.AdManager;
import org.alexdev.havana.game.badges.Badge;
import org.alexdev.havana.game.catalogue.CatalogueManager;
import org.alexdev.havana.game.events.Event;
import org.alexdev.havana.game.events.EventsManager;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.games.GameManager;
import org.alexdev.havana.game.games.enums.GameType;
import org.alexdev.havana.game.groups.GroupMember;
import org.alexdev.havana.game.infobus.InfobusManager;
import org.alexdev.havana.game.item.Item;
import org.alexdev.havana.game.item.ItemManager;
import org.alexdev.havana.game.item.base.ItemBehaviour;
import org.alexdev.havana.game.messenger.MessengerUser;
import org.alexdev.havana.game.moderation.cfh.CallForHelp;
import org.alexdev.havana.game.moderation.cfh.CallForHelpManager;
import org.alexdev.havana.game.moderation.cfh.enums.CFHAction;
import org.alexdev.havana.game.navigator.NavigatorManager;
import org.alexdev.havana.game.pathfinder.Position;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.game.wordfilter.WordfilterManager;
import org.alexdev.havana.log.Log;
import org.alexdev.havana.messages.flash.outgoing.FLASH_ROOMENTRYINFO;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_MODTOOL;
import org.alexdev.havana.messages.incoming.catalogue.GET_CATALOG_INDEX;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.outgoing.events.ROOMEEVENT_INFO;
import org.alexdev.havana.messages.outgoing.games.GAMEPLAYERINFO;
import org.alexdev.havana.messages.outgoing.games.LOUNGEINFO;
import org.alexdev.havana.messages.outgoing.handshake.RIGHTS;
import org.alexdev.havana.messages.outgoing.moderation.CRY_REPLY;
import org.alexdev.havana.messages.outgoing.moderation.MODERATOR_ALERT;
import org.alexdev.havana.messages.outgoing.moderation.PICKED_CRY;
import org.alexdev.havana.messages.outgoing.rooms.ROOMQUEUEDATA;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_BADGES;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_MEMBERSHIP_UPDATE;
import org.alexdev.havana.messages.outgoing.rooms.user.HOTEL_VIEW;
import org.alexdev.havana.messages.outgoing.user.currencies.CREDIT_BALANCE;
import org.alexdev.havana.server.rcon.messages.RconMessage;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.havana.util.config.writer.GameConfigWriter;
import org.alexdev.havana.util.housekeeping.MessageDecoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RconConnectionHandler extends ChannelInboundHandlerAdapter {
    final private static Logger log = LoggerFactory.getLogger(RconConnectionHandler.class);

    private final RconServer server;

    public RconConnectionHandler(RconServer rconServer) {
        this.server = rconServer;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        if (!this.server.getChannels().add(ctx.channel()) || Havana.isShuttingdown()) {
            //Log.getErrorLogger().error("Could not accept RCON connection from {}", ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0]);
            ctx.close();
        }

        //log.info("[RCON] Connection from {}", ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0]);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        this.server.getChannels().remove(ctx.channel());
        //log.info("[RCON] Disconnection from {}", ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0]);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof RconMessage)) {
            return;
        }

        RconMessage message = (RconMessage) msg;

        //log.info("[RCON] Message received: " + message);

        try {
            switch (message.getHeader()) {
                case DISCONNECT_USER:
                    Player online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.getNetwork().disconnect();
                    }

                    break;
                case REFRESH_LOOKS:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.getRoomUser().refreshAppearance();
                    }

                    break;
                case CFH_PICK:
                    int callId = Integer.parseInt(message.getValues().get("cryId"));
                    Player moderator = PlayerManager.getInstance().getPlayerByName(message.getValues().get("moderator"));
                    CallForHelp call = CallForHelpManager.getInstance().getCall(callId);

                    if (call ==  null) {
                        return;
                    }

                    if (moderator ==  null) {
                        return;
                    }

                    CallForHelpManager.getInstance().pickUp(call, moderator);

                    for (Player player : PlayerManager.getInstance().getPlayers()) {
                        if (player.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {

                            player.send(new PICKED_CRY(call));
                            //player.send(new DELETE_CRY(cfh));
                            //player.send(new DELETE_CRY(callId));
                        }
                    }
                    break;
                case CFH_REPLY:
                    Player moderatorReply = PlayerManager.getInstance().getPlayerByName(message.getValues().get("moderatorReply"));

                    if (moderatorReply == null) {
                        return;
                    }

                    if (!moderatorReply.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
                        return;
                    }

                    int callIdReply = Integer.parseInt(message.getValues().get("cryIdReply"));

                    CallForHelp cfh = CallForHelpManager.getInstance().getCall(callIdReply);

                    String messageReplyEncoded = message.getValues().get("messageReply");
                    String messageReplyDecoded = MessageDecoderUtil.decodeMessage(messageReplyEncoded);

                    if (cfh == null) {
                        return;
                    }

                    Player caller = PlayerManager.getInstance().getPlayerById(cfh.getCaller());

                    if (caller == null) {
                        return;
                    }

                    caller.send(new CRY_REPLY(messageReplyDecoded));
                    CallForHelpManager.getInstance().deleteCall(cfh, CFHAction.REPLY, messageReplyDecoded);
                    break;
                case CFH_BLOCK:
                    Player moderatorBlock = PlayerManager.getInstance().getPlayerByName(message.getValues().get("moderatorBlock"));

                    if (moderatorBlock ==  null) {
                        return;
                    }

                    if (!moderatorBlock.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
                        return;
                    }

                    int callIdDelete = Integer.parseInt(message.getValues().get("cryIdBlock"));

                    CallForHelp cfhDelete = CallForHelpManager.getInstance().getCall(callIdDelete);

                    if (cfhDelete == null) {
                        return;
                    }

                    CallForHelpManager.getInstance().deleteCall(cfhDelete, CFHAction.BLOCK, null);
                    break;
                case CFH_FOLLOW:
                    Player moderatorFollow = PlayerManager.getInstance().getPlayerByName(message.getValues().get("moderatorFollow"));

                    if (moderatorFollow ==  null) {
                        return;
                    }

                    if (!moderatorFollow.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
                        return;
                    }

                    int callIdFollow = Integer.parseInt(message.getValues().get("cryIdFollow"));

                    CallForHelp cfhFollow = CallForHelpManager.getInstance().getCall(callIdFollow);

                    if (cfhFollow == null) {
                        return;
                    }

                    if (cfhFollow.getRoom() == null) {
                        return;
                    }

                    cfhFollow.getRoom().forward(moderatorFollow, false);
                    CallForHelpManager.getInstance().deleteCall(cfhFollow, CFHAction.FOLLOW, null);
                    break;
                case COPY_PRIVATE_ROOM:
                    String moderatorCopy = message.getValues().get("moderator");
                    int roomIdToCopy = Integer.parseInt(message.getValues().get("roomId"));
                    var playerCopy = PlayerDao.getDetails(moderatorCopy);
                    var roomToCopy = RoomManager.getInstance().getRoomById(roomIdToCopy);

                    if (playerCopy == null || roomToCopy == null || roomToCopy.isPublicRoom()) {
                        return;
                    }

                    var roomName = roomToCopy.getData().getName() + " (2)";
                    var roomModel = roomToCopy.getModel().getName();
                    var roomShowName = roomToCopy.getData().showOwnerName();
                    var accessType = roomToCopy.getData().getAccessTypeId();

                    int roomIdCopy = -1;
                    try {
                        roomIdCopy = NavigatorDao.createRoom(playerCopy.getId(), roomName, roomModel, roomShowName, accessType);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if (roomIdCopy == -1)
                        return;

                    var copyRoom = RoomDao.getRoomById(roomIdCopy);

                    copyRoom.getData().setWallpaper(roomToCopy.getData().getWallpaper());
                    copyRoom.getData().setFloor(roomToCopy.getData().getFloor());
                    copyRoom.getData().setLandscape(roomToCopy.getData().getLandscape());

                    RoomDao.saveDecorations(copyRoom);

                    List<Item> items = new ArrayList<>();

                    for (Item item : roomToCopy.getItems()) {
                        if (item.hasBehaviour(ItemBehaviour.TELEPORTER)) {
                            continue;
                        }

                        var copyItem = new Item();
                        copyItem.setOwnerId(playerCopy.getId());
                        copyItem.setDefinitionId(item.getDefinition().getId());
                        copyItem.setCustomData(item.getCustomData());
                        copyItem.setRoomId(roomIdCopy);

                        if (item.hasBehaviour(ItemBehaviour.WALL_ITEM))
                            copyItem.setWallPosition(item.getWallPosition());
                        else {
                            copyItem.getPosition().setX(item.getPosition().getX());
                            copyItem.getPosition().setY(item.getPosition().getY());
                            copyItem.getPosition().setZ(item.getPosition().getZ());
                            copyItem.getPosition().setRotation(item.getPosition().getRotation());
                        }

                        try {
                            ItemDao.newItem(copyItem);
                            items.add(copyItem);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    ItemDao.updateItems(items);

                    Player playerCopy2 = PlayerManager.getInstance().getPlayerByName(moderatorCopy);

                    if (playerCopy2 == null) {
                        return;
                    }

                    copyRoom.forward(playerCopy2, false);
                    break;
                case MOD_ROOM_KICK:
                    int roomId = Integer.parseInt(message.getValues().get("roomId"));
                    Room roomKick = RoomManager.getInstance().getRoomById(roomId);
                    String messageRoomKickEncoded = message.getValues().get("alertRoomKick");
                    String messageRomKickDecoded = MessageDecoderUtil.decodeMessage(messageRoomKickEncoded);
                    String actionRoomKick = message.getValues().get("action");
                    boolean unacceptable = Boolean.parseBoolean(message.getValues().get("unacceptable"));
                    String unacceptableValue = message.getValues().get("unacceptableValue");
                    String unacceptableDescValue = message.getValues().get("unacceptableDescValue");
                    boolean roomLock = Boolean.parseBoolean(message.getValues().get("roomLock"));


                    if (roomKick == null) {
                        return;
                    }

                    if (roomLock && !roomKick.isPublicRoom()) {
                        roomKick.getData().setAccessType(1);
                        RoomDao.save(roomKick);
                    }

                    if (unacceptable && !roomKick.isPublicRoom()) {
                        roomKick.getData().setName(unacceptableValue);
                        roomKick.getData().setDescription(unacceptableDescValue);
                        RoomDao.save(roomKick);
                    }

                    List<Player> players = RoomManager.getInstance().getRoomById(roomId).getEntityManager().getPlayers();

                    for (Player target : players) {
                        target.send(new MODERATOR_ALERT(messageRomKickDecoded));

                        // Don't kick other moderators
                        if (target.hasFuse(Fuseright.ROOM_KICK)) {
                            continue;
                        }

                        if (actionRoomKick.equals("kick")) {
                            target.getRoomUser().kick(true, true);
                            //target.send(new HOTEL_VIEW());

                            if (!EventsManager.getInstance().hasEvent(roomKick.getId())) {
                                return;
                            }

                            Event event =  EventsManager.getInstance().getEventByRoomId(roomKick.getId());

                            if (event == null) {
                                return;
                            }

                            EventsManager.getInstance().removeEvent(event);
                            roomKick.send(new ROOMEEVENT_INFO(null));
                        }
                    }
                    break;
                case MOD_GIVE_BADGE:
                    String user = message.getValues().get("user");
                    String badge = message.getValues().get("badge");
                    String removeBadge = message.getValues().get("removeBadge");

                    PlayerDetails targetUserDetails = PlayerDao.getDetails(user);

                    Player targetUser = PlayerManager.getInstance().getPlayerByName(user);

                    if (targetUserDetails == null) {
                        return;
                    }

                    if (targetUser == null) {
                        List<Badge> userBadges = BadgeDao.getBadges(targetUserDetails.getId());

                        boolean hasRemoveBadge = userBadges.stream()
                                .anyMatch(b -> b.getBadgeCode().equalsIgnoreCase(removeBadge));

                        boolean alreadyHasBadge = userBadges.stream()
                                .anyMatch(b -> b.getBadgeCode().equalsIgnoreCase(badge));

                        if (removeBadge.length() > 0 && hasRemoveBadge) {
                            BadgeDao.removeBadge(targetUserDetails.getId(), removeBadge);
                        }

                        if (badge.length() > 0 && !alreadyHasBadge) {
                            BadgeDao.newBadge(targetUserDetails.getId(), badge);
                        }
                    } else {
                        if (badge.length() > 0) {
                            targetUser.getBadgeManager().tryAddBadge(badge, removeBadge, 0);
                        }

                        if (removeBadge.length() > 0) {
                            targetUser.getBadgeManager().removeBadge(removeBadge);
                        }

                        targetUser.getBadgeManager().refreshBadges();
                    }
                    break;
                case MOD_STICKIE_DELETE:
                    int stickieId = Integer.parseInt(message.getValues().get("stickieId"));
                    String stickieTextEncoded = message.getValues().get("stickieText");
                    String stickieTextDecoded = MessageDecoderUtil.decodeMessage(stickieTextEncoded);
                    boolean deleteStickie = Boolean.parseBoolean(message.getValues().get("deleteStickie"));

                    if (stickieId <= 0) {
                        return;
                    }

                    if (stickieTextDecoded == null) {
                        return;
                    }

                    if (ItemManager.getInstance().resolveItem(stickieId) == null) {
                        return;
                    }

                    ItemManager.getInstance().resolveItem(stickieId).setCustomData(stickieTextDecoded);
                    ItemManager.getInstance().resolveItem(stickieId).updateStatus();
                    ItemDao.stickieNoteModerateText(stickieTextDecoded, stickieId);
                    ItemManager.reset();

                    if (deleteStickie) {
                        Room roomStickie = ItemManager.getInstance().resolveItem(stickieId).getRoom();

                        Item stickieItem = ItemManager.getInstance().resolveItem(stickieId);
                        List<Player> playersStickie = PlayerManager.getInstance().getPlayers();

                        if (roomStickie == null) {
                            return;
                        }

                        if (stickieItem == null) {
                            return;
                        }

                        if (playersStickie == null) {
                            return;
                        }

                        for (Player target : playersStickie) {
                            roomStickie.getMapping().removeItem(stickieItem);
                            stickieItem.delete();

                            target.getRoomUser().getTimerManager().resetRoomTimer();
                        }
                    }
                    ItemManager.reset();
                    break;
                case REFRESH_SETTINGS:
                    GameConfiguration.reset(new GameConfigWriter());
                    break;
                case REFRESH_GAMESRANKS:
                    List<Player> playersGamesRanks = PlayerManager.getInstance().getPlayers();
                    var roomBattleBall = RoomManager.getInstance().getRoomByModel(GameType.BATTLEBALL.getLobbyModel());
                    var roomSnowStorm = RoomManager.getInstance().getRoomByModel(GameType.SNOWSTORM.getLobbyModel());

                    for (Player player : playersGamesRanks) {
                        roomBattleBall.send(new GAMEPLAYERINFO(GameType.BATTLEBALL, List.of(player)));
                        roomSnowStorm.send(new GAMEPLAYERINFO(GameType.SNOWSTORM, List.of(player)));
                        player.send(new LOUNGEINFO());
                    }

                    GameManager.reset();
                    break;
                case REFRESH_CFH_TOPICS:
                    for (Player player : PlayerManager.getInstance().getPlayers()) {
                        if (player.getNetwork().isFlashConnection()) {
                            if (player.getDetails().getRank().getRankId() >= PlayerRank.MODERATOR.getRankId()) {
                                player.send(new FLASH_MODTOOL());
                            }
                        }
                    }
                    break;
                case REFRESH_CATALOGUE_PAGES:
                    ItemManager.reset();
                    CatalogueManager.reset();

                    for (Player p : PlayerManager.getInstance().getPlayers()) {
                        new GET_CATALOG_INDEX().handle(p, null);
                        p.send(new ALERT(GameConfiguration.getInstance().getString("rcon.catalogue.refresh.message")));
                    }
                case REFRESH_NAVIGATOR:
                    NavigatorManager.getInstance().reset();
                    NavigatorManager.reset();
                    break;
                case REFRESH_PRIVATE_ROOM:
                    int roomEditId = Integer.parseInt(message.getValues().get("room"));
                    int category = Integer.parseInt(message.getValues().get("category"));
                    String name = message.getValues().get("name");
                    String description = message.getValues().get("description");
                    int accesstype = Integer.parseInt(message.getValues().get("accesstype"));
                    String password = message.getValues().get("password");
                    boolean showOwnerName = Boolean.parseBoolean(message.getValues().get("showOwnerName"));


                    var roomEdit = RoomDao.getRoomById(roomEditId);

                    roomEdit.getData().setCategoryId(category);
                    roomEdit.getData().setName(name);
                    roomEdit.getData().setDescription(description);
                    roomEdit.getData().setAccessType(accesstype);
                    roomEdit.getData().setPassword(password);
                    roomEdit.getData().setShowOwnerName(showOwnerName);
                    RoomDao.save(roomEdit);

                    NavigatorManager.getInstance().reset();
                    NavigatorManager.reset();
                    break;
                case REFRESH_WORDFILTER:
                    WordfilterManager.reset();
                    break;
                case MOD_ALERT_USER:
                    Player playerAlert = PlayerManager.getInstance().getPlayerByName(message.getValues().get("receiver"));
                    String messageAlertUserEncoded = message.getValues().get("message");
                    String messageAlertUserDecoded = MessageDecoderUtil.decodeMessage(messageAlertUserEncoded);;

                    if (playerAlert != null) {
                        playerAlert.send(new MODERATOR_ALERT(messageAlertUserDecoded));
                    }
                    break;
                case MOD_KICK_USER:
                    String playerKick = message.getValues().get("receiver");
                    Player target = PlayerManager.getInstance().getPlayerByName(playerKick);
                    String messageKickUserEncoded = message.getValues().get("message");
                    String messageKickUserDecoded = MessageDecoderUtil.decodeMessage(messageKickUserEncoded);

                    if (!target.getNetwork().isFlashConnection()) {
                        target = PlayerManager.getInstance().getPlayerByName(playerKick);
                    } else {
                        target = PlayerManager.getInstance().getPlayerById(target.getDetails().getId());
                    }

                    if (target != null) {
                        if (target.getRoomUser().getRoom() != null) {
                            target.getRoomUser().kick(true, true);
                            //target.send(new HOTEL_VIEW());
                            target.send(new MODERATOR_ALERT(messageKickUserDecoded));
                        }
                    }
                    break;
                case HOTEL_ALERT:
                    String messageSender = message.getValues().get("sender");
                    String messageHotelAlertEncoded = message.getValues().get("message");
                    String messageHotelAlertDecoded = MessageDecoderUtil.decodeMessage(messageHotelAlertEncoded);;
                    boolean showSender = Boolean.parseBoolean(message.getValues().get("showSender"));

                    StringBuilder alert = new StringBuilder();
                    alert.append(messageHotelAlertDecoded).append("<br>");

                    if (showSender) {
                        alert.append("<br>");
                        alert.append("- ").append(messageSender);
                    }

                    for (Player player : PlayerManager.getInstance().getPlayers()) {
                        player.send(new ALERT(alert.toString()));
                    }
                    break;
                case REFRESH_CLUB:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        PlayerDetails playerDetails = PlayerDao.getDetails(online.getDetails().getId());

                        online.getDetails().setCredits(playerDetails.getCredits());
                        online.getDetails().setClubExpiration(playerDetails.getClubExpiration());
                        online.getDetails().setFirstClubSubscription(playerDetails.getFirstClubSubscription());

                        PlayerDao.saveCurrency(online.getDetails().getId(), playerDetails.getCredits(), online.getDetails().getPixels());

                        online.send(new CREDIT_BALANCE(online.getDetails().getCredits()));
                        online.send(new RIGHTS(online.getFuserights()));
                        online.refreshClub();

                        AchievementManager.getInstance().tryProgress(AchievementType.ACHIEVEMENT_HC, online);
                    }

                    break;

                case REFRESH_TAGS:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        AchievementManager.getInstance().tryProgress(AchievementType.ACHIEVEMENT_TAGS, online);

                        if (online.getRoomUser().getRoom() != null) {
                            online.getRoomUser().refreshTags();
                        }
                    }
                    break;
                case REFRESH_HAND:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.getInventory().reload();

                        if (online.getRoomUser().getRoom() != null)
                            online.getInventory().getView("new");
                    }

                    break;
                case REFRESH_CREDITS:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.getDetails().setCredits(CurrencyDao.getCredits(online.getDetails().getId()));
                        online.send(new CREDIT_BALANCE(online.getDetails().getCredits()));
                    }

                    break;
                case FRIEND_REQUEST:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("friendId")));
                    int requestFrom = Integer.parseInt(message.getValues().get("userId"));

                    if (online != null) {
                        if (!online.getMessenger().hasRequest(requestFrom)) {
                            online.getMessenger().addRequest(new MessengerUser(PlayerManager.getInstance().getPlayerData(requestFrom)));
                        }
                    }

                    break;
                case REFRESH_MESSENGER_CATEGORIES:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.getMessenger().getCategories().clear();
                        online.getMessenger().getCategories().addAll(MessengerDao.getCategories(online.getDetails().getId()));

                        // Refresh friends for categories
                        for (MessengerUser dbFriend : MessengerDao.getFriends(online.getDetails().getId()).values()) {
                            MessengerUser friend = online.getMessenger().getFriend(dbFriend.getUserId());

                            if (friend != null) {
                                if (friend.getCategoryId() != dbFriend.getCategoryId()) {
                                    friend.setCategoryId(dbFriend.getCategoryId());
                                    online.getMessenger().queueFriendUpdate(friend);
                                }
                            }
                        }
                    }

                    break;
                case REFRESH_TRADE_SETTING:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        boolean oldTradeSetting = online.getDetails().isTradeEnabled();
                        online.getDetails().setTradeEnabled(message.getValues().get("tradeEnabled").equalsIgnoreCase("1"));

                        if (!oldTradeSetting && online.getDetails().isTradeEnabled()) {
                            if (online.getRoomUser().getRoom() != null &&
                                    !online.getRoomUser().getRoom().isGameArena()) {
                                Position currentPosition = online.getRoomUser().getPosition();

                                online.getRoomUser().getRoom().getEntityManager().enterRoom(online, currentPosition);
                                online.getRoomUser().invokeItem(null, false);
                            }
                        }
                    }
                    break;
                case REFRESH_GROUP_PERMS:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.refreshJoinedGroups();

                        PlayerDetails newDetails = PlayerDao.getDetails(online.getDetails().getId());
                        online.getDetails().setFavouriteGroupId(newDetails.getFavouriteGroupId());

                        int newGroup = newDetails.getFavouriteGroupId();

                        if (online.getRoomUser().getRoom() != null) {
                            GroupMember groupMember = null;

                            if (online.getDetails().getFavouriteGroupId() > 0) {
                                groupMember = online.getDetails().getGroupMember();
                            }

                            if (groupMember != null) {
                                online.getRoomUser().getRoom().send(new GROUP_BADGES(new HashMap<>() {{
                                    put(newGroup, online.getJoinedGroup(newGroup).getBadge());
                                }}));
                            }

                            online.getRoomUser().getRoom().send(new GROUP_MEMBERSHIP_UPDATE(online.getRoomUser().getInstanceId(), groupMember == null ? -1 : groupMember.getGroupId(), groupMember == null ? -1 : groupMember.getMemberRank().getClientRank()));
                        }
                    }

                    break;
                case GROUP_DELETED:
                    int groupId = Integer.parseInt(message.getValues().get("groupId"));

                    for (Player player : PlayerManager.getInstance().getPlayers()) {
                        if (player.getDetails().getFavouriteGroupId() == groupId) {
                            player.getDetails().setFavouriteGroupId(0);

                            if (player.getRoomUser().getRoom() != null) {
                                player.getRoomUser().getRoom().send(new GROUP_MEMBERSHIP_UPDATE(player.getRoomUser().getInstanceId(), -1, -1));
                            }

                            player.refreshJoinedGroups();
                        }
                    }
                    break;
                case REFRESH_GROUP:
                    groupId = Integer.parseInt(message.getValues().get("groupId"));

                    for (Player player : PlayerManager.getInstance().getPlayers()) {
                        if (player.getJoinedGroup(groupId) != null) {
                            player.refreshJoinedGroups();
                        }
                    }

                    break;
                case REFRESH_ADS:
                    AdManager.getInstance().reset();
                    break;
                case REFRESH_ROOM_BADGES:
                    RoomManager.getInstance().reloadBadges();
                    RoomManager.getInstance().giveBadges();
                    break;
                case INFOBUS_DOOR_STATUS:
                    InfobusManager.getInstance().updateDoorStatus(message.getValues().get("doorStatus").equals("1"));
                    break;
                case INFOBUS_END_EVENT:
                    InfobusManager.getInstance().stopEvent();
                    break;
                case INFOBUS_POLL:
                    int pollId = Integer.parseInt(message.getValues().get("pollId"));
                    InfobusManager.getInstance().startPolling(pollId);
                    break;
                case REFRESH_CATALOGUE_FRONTPAGE:
                    GameConfiguration.reset(new GameConfigWriter());

                    for (Player p : PlayerManager.getInstance().getPlayers()) {
                        new GET_CATALOG_INDEX().handle(p, null);
                    }

                    break;
                case CLEAR_PHOTO:
                    long itemId = Long.parseLong(message.getValues().get("itemId"));
                    int userId = Integer.parseInt(message.getValues().get("userId"));

                    Item item = ItemManager.getInstance().resolveItem(itemId);

                    if (item != null) {
                        Room room = item.getRoom();

                        if (room != null) {
                            room.getMapping().removeItem(item);
                        }

                        item.delete();
                        PhotoDao.deleteItem(itemId);

                        TransactionDao.createTransaction(userId, String.valueOf(itemId), "0", 1,
                                "Hidden photo " + itemId, 0, 0, false);
                    }

                    break;
                case REFRESH_STATISTICS:
                    online = PlayerManager.getInstance().getPlayerById(Integer.parseInt(message.getValues().get("userId")));

                    if (online != null) {
                        online.getStatisticManager().reload();
                    }

                    break;
            }
        } catch (Exception ex) {
            Log.getErrorLogger().error("[RCON] Error occurred when handling RCON message: ", ex);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof Exception) {
            if (!(cause instanceof IOException)) {
                Log.getErrorLogger().error("[RCON] Error occurred: ", cause);
            }
        }

        ctx.close();
    }
}
