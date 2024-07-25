package org.alexdev.havana.server.rcon;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.alexdev.havana.Havana;
import org.alexdev.havana.dao.mysql.*;
import org.alexdev.havana.game.GameScheduler;
import org.alexdev.havana.game.achievements.AchievementManager;
import org.alexdev.havana.game.achievements.AchievementType;
import org.alexdev.havana.game.ads.AdManager;
import org.alexdev.havana.game.catalogue.CatalogueManager;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.groups.GroupMember;
import org.alexdev.havana.game.infobus.InfobusManager;
import org.alexdev.havana.game.item.Item;
import org.alexdev.havana.game.item.ItemManager;
import org.alexdev.havana.game.messenger.MessengerUser;
import org.alexdev.havana.game.moderation.cfh.CallForHelp;
import org.alexdev.havana.game.moderation.cfh.CallForHelpManager;
import org.alexdev.havana.game.navigator.NavigatorManager;
import org.alexdev.havana.game.pathfinder.Position;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.log.Log;
import org.alexdev.havana.messages.incoming.catalogue.GET_CATALOG_INDEX;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.outgoing.handshake.RIGHTS;
import org.alexdev.havana.messages.outgoing.moderation.CRY_REPLY;
import org.alexdev.havana.messages.outgoing.moderation.MODERATOR_ALERT;
import org.alexdev.havana.messages.outgoing.moderation.PICKED_CRY;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_BADGES;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_MEMBERSHIP_UPDATE;
import org.alexdev.havana.messages.outgoing.user.currencies.CREDIT_BALANCE;
import org.alexdev.havana.server.rcon.messages.RconMessage;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.havana.util.config.writer.GameConfigWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

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

                    if (moderatorReply ==  null) {
                        return;
                    }

                    if (!moderatorReply.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
                        return;
                    }

                    int callIdReply = Integer.parseInt(message.getValues().get("cryIdReply"));

                    CallForHelp cfh = CallForHelpManager.getInstance().getCall(callIdReply);

                    String messageReply = message.getValues().get("messageReply");

                    if (cfh == null) {
                        return;
                    }

                    Player caller = PlayerManager.getInstance().getPlayerById(cfh.getCaller());

                    if (caller == null) {
                        return;
                    }

                    CFHDao.updateReplyType(cfh, "REPLY", messageReply);

                    caller.send(new CRY_REPLY(messageReply));
                    CallForHelpManager.getInstance().deleteCall(cfh);
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

                    CFHDao.updateReplyType(cfhDelete, "BLOCK", "");

                    CallForHelpManager.getInstance().deleteCall(cfhDelete);
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

                    CFHDao.updateReplyType(cfhFollow, "FOLLOW", "");

                    cfhFollow.getRoom().forward(moderatorFollow, false);
                    CallForHelpManager.getInstance().deleteCall(cfhFollow);
                    break;
                case MOD_STICKIE_DELETE:
                    int stickieId = Integer.parseInt(message.getValues().get("stickieId"));
                    String stickieText = message.getValues().get("stickieText");
                    boolean deleteStickie = Boolean.parseBoolean(message.getValues().get("deleteStickie"));

                    if (stickieId <= 0) {
                        return;
                    }

                    if (stickieText == null) {
                        return;
                    }

                    if (ItemManager.getInstance().resolveItem(stickieId) == null) {
                        return;
                    }

                    ItemManager.getInstance().resolveItem(stickieId).setCustomData(stickieText);
                    ItemManager.getInstance().resolveItem(stickieId).updateStatus();
                    ItemDao.stickieNoteModerateText(stickieText, stickieId);
                    ItemManager.reset();

                    if (deleteStickie) {
                        GameScheduler.getInstance().queueDeleteItem((long) stickieId);
                        GameScheduler.getInstance().performItemDeletion();
                        ItemManager.reset();
                    }
                    break;
                case REFRESH_CATALOGUE_PAGES:
                    CatalogueManager.reset();
                    break;
                case REFRESH_NAVIGATOR:
                    NavigatorManager.getInstance().reset();
                    NavigatorManager.reset();
                    break;
                case MOD_ALERT_USER:
                    Player playerAlert = PlayerManager.getInstance().getPlayerByName(message.getValues().get("receiver"));
                    String alertMessageUser = message.getValues().get("message");

                    if (playerAlert != null) {
                        playerAlert.send(new MODERATOR_ALERT(alertMessageUser));
                    }
                    break;
                case MOD_KICK_USER:
                    Player target = PlayerManager.getInstance().getPlayerByName(message.getValues().get("receiver"));
                    String alertMessage = message.getValues().get("message");

                    if (target != null) {
                        if (target.getRoomUser().getRoom() != null) {
                            target.getRoomUser().kick(true, true);
                            //target.send(new HOTEL_VIEW());
                            target.send(new MODERATOR_ALERT(alertMessage));
                        }
                    }
                    break;
                case HOTEL_ALERT:
                    String messageSender = message.getValues().get("sender");
                    String hotelAlert = message.getValues().get("message");

                    StringBuilder alert = new StringBuilder();
                    alert.append(hotelAlert).append("<br>");
                    alert.append("<br>");
                    alert.append("- ").append(messageSender);

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
                        if(PlayerDao.hasDiscordId(online.getDetails().getId())) {
                            AchievementManager.getInstance().tryProgress(AchievementType.ACHIEVEMENT_EMAIL_VERIFICATION, online);
                        }
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
                        new GET_CATALOG_INDEX(false).handle(p, null);
                    }

                    break;
                case CLEAR_PHOTO:
                    long itemId = Long.parseLong(message.getValues().get("itemId"));
                    int userId = Integer.parseInt(message.getValues().get("userId"));

                    Item item = ItemManager.getInstance().resolveItem(itemId);

                    if (item != null) {
                        Room room = item.getRoom();

                        if (room != null) {
                            room.getMapping().removeItem(null, item);
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
