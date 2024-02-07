package org.alexdev.havana.game.player;

import com.google.gson.JsonObject;
import io.netty.util.AttributeKey;
import org.alexdev.havana.Havana;
import org.alexdev.havana.dao.mysql.*;
import org.alexdev.havana.game.achievements.user.UserAchievementManager;
import org.alexdev.havana.game.badges.BadgeManager;
import org.alexdev.havana.game.club.ClubSubscription;
import org.alexdev.havana.game.effects.Effect;
import org.alexdev.havana.game.encryption.DiffieHellman;
import org.alexdev.havana.game.encryption.RC4;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.fuserights.FuserightsManager;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.havana.game.groups.GroupMemberRank;
import org.alexdev.havana.game.guides.GuideManager;
import org.alexdev.havana.game.inventory.FlashInventory;
import org.alexdev.havana.game.inventory.Inventory;
import org.alexdev.havana.game.item.ItemManager;
import org.alexdev.havana.game.messenger.Messenger;
import org.alexdev.havana.game.player.guides.PlayerGuideManager;
import org.alexdev.havana.game.player.statistics.PlayerStatistic;
import org.alexdev.havana.game.player.statistics.PlayerStatisticManager;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.game.room.entities.RoomPlayer;
import org.alexdev.havana.messages.incoming.club.SCR_GIFT_APPROVAL;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.outgoing.alerts.HOTEL_LOGOUT;
import org.alexdev.havana.messages.outgoing.alerts.HOTEL_LOGOUT.LogoutReason;
import org.alexdev.havana.messages.outgoing.club.CLUB_GIFT;
import org.alexdev.havana.messages.outgoing.effects.AVATAR_EFFECTS;
import org.alexdev.havana.messages.outgoing.handshake.*;
import org.alexdev.havana.messages.outgoing.openinghours.INFO_HOTEL_CLOSING;
import org.alexdev.havana.messages.outgoing.user.settings.HELP_ITEMS;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.NettyPlayerNetwork;
import org.alexdev.havana.util.DateUtil;
import org.alexdev.havana.util.StringUtil;
import org.alexdev.havana.util.config.GameConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static java.util.Map.entry;

public class Player extends Entity {
    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("Player");

    private final NettyPlayerNetwork network;
    private final PlayerDetails details;
    private final RoomPlayer roomEntity;

    private List<String> chatHistory = new ArrayList<String>();

    private Logger log;
    private Messenger messenger;
    private Inventory inventory;
    private BadgeManager badgeManager;
    private UserAchievementManager achievementManager;
    private PlayerGuideManager guideManager;
    private PlayerStatisticManager statisticManager;

    private CopyOnWriteArrayList<Effect> effects;
    private Set<String> ignoredList;

    private DiffieHellman diffieHellman;
    private RC4 rc4;

    private boolean loggedIn;
    private boolean disconnected;
    private boolean pingOK;
    private boolean hasGenerateKey;
    private long timeConnected;
    private boolean processLoginSteps;
    private List<Group> joinedGroups;
    private String lastGift;

    public boolean flash;
    private boolean hasLoadedCatalogue;
    private String countryBadgeToGive;
    private boolean hasBeenCreditsWarned;
    private boolean hasBeenPixelsWarned;

    public Player(NettyPlayerNetwork nettyPlayerNetwork, boolean flash) {
        this.network = nettyPlayerNetwork;
        this.details = new PlayerDetails();
        this.badgeManager = new BadgeManager();
        this.diffieHellman = new DiffieHellman();
        this.roomEntity = new RoomPlayer(this);
        this.guideManager = new PlayerGuideManager(this);
        this.statisticManager = new PlayerStatisticManager(-1, Map.of());
        this.achievementManager = new UserAchievementManager();
        this.effects = new CopyOnWriteArrayList<>();
        this.ignoredList = new HashSet<>();
        this.log = LoggerFactory.getLogger("Connection " + this.network.getConnectionId());
        this.pingOK = true;
        this.disconnected = false;
        this.processLoginSteps = true;
        this.flash = flash;
        this.hasLoadedCatalogue = false;
        this.countryBadgeToGive = null;
        hasBeenCreditsWarned = false;
        hasBeenPixelsWarned = false;
    }

    /**
     * Login handler for player
     */
    public void login() {
        this.log = LoggerFactory.getLogger("Player " + this.details.getName()); // Update logger to show name
        this.loggedIn = true;
        this.pingOK = true;
        this.timeConnected = DateUtil.getCurrentTimeSeconds();

        PlayerManager.getInstance().disconnectSession(this.details.getId()); // Kill other sessions with same id
        PlayerManager.getInstance().addPlayer(this); // Add new connection

        this.details.setLastOnline(DateUtil.getCurrentTimeSeconds());

        if (!this.details.getName().equals("Abigail.Ryan")) {
            PlayerDao.saveLastOnline(this.details.getId(), this.details.getLastOnline(), true);
        }

        SettingsDao.updateSetting("players.online", String.valueOf(PlayerManager.getInstance().getPlayers().size()));

        if (GameConfiguration.getInstance().getBoolean("reset.sso.after.login")) {
            PlayerDao.resetSsoTicket(this.details.getId()); // Protect against replay attacks
        }

        this.messenger = new Messenger(this);
        if(this.flash) {
            this.inventory = new FlashInventory(this, 100000);
        } else {
            this.inventory = new Inventory(this, 9);
        }

        // Bye bye!
        if (this.getDetails().isBanned() != null) {
            this.kickFromServer();
            return;
        }

        if (this.details.getMachineId() == null || this.details.getMachineId().isBlank() || !(
                this.details.getMachineId().length() == 33 &&
                this.details.getMachineId().startsWith("#"))) {
            this.details.setMachineId(this.network.getClientMachineId());
            this.network.setSaveMachineId(true);
            PlayerDao.setMachineId(this.details.getId(), this.details.getMachineId());
        }

        if (this.network.saveMachineId()) {
            this.send(new UniqueIDMessageEvent(this.network.getClientMachineId()));
        }

        // Update user IP address
        String ipAddress = NettyPlayerNetwork.getIpAddress(this.getNetwork().getChannel());
        var latestIp = PlayerDao.getLatestIp(this.details.getId());

        if (latestIp == null || !latestIp.equals(ipAddress)) {
            PlayerDao.logIpAddress(this.getDetails().getId(), ipAddress);
        }

        EffectDao.removeEffects(this.details.getId());
        this.effects = EffectDao.getEffects(this.details.getId());

        if (!this.getDetails().getRespectDay().equals(DateUtil.getCurrentDate(DateUtil.SHORT_DATE))) {
            this.getDetails().setRespectDay(DateUtil.getCurrentDate(DateUtil.SHORT_DATE));
            this.getDetails().setDailyRespectPoints(3);

            this.getDetails().setCreditsEligible(true);
            CurrencyDao.updateEligibleCredits(this.details.getId(), true);

            PlayerDao.saveRespect(this.details.getId(), this.details.getDailyRespectPoints(), this.details.getRespectPoints(), this.details.getRespectDay(), this.details.getRespectGiven());

            //CurrencyDao.increasePixels(this.details, 15);
            //this.send(new ActivityPointNotification(this.details.getPixels(), ActivityPointNotification.ActivityPointAlertType.PIXELS_RECEIVED)); // Alert pixels received
        }

        // Do pixels
        TimeUnit pixelsReceived = TimeUnit.valueOf(GameConfiguration.getInstance().getString("pixels.received.timeunit"));
        int intervalInSeconds = (int) pixelsReceived.toSeconds(GameConfiguration.getInstance().getInteger("pixels.received.interval"));
        this.details.setLastPixelsTime(DateUtil.getCurrentTimeSeconds() + intervalInSeconds);

        // Set trade ban back to 0, easier for db querying
        if (this.details.getTradeBanExpiration() > 0 && !this.details.isTradeBanned()) {
            this.details.setTradeBanExpiration(0);
            ItemDao.saveTradeBanExpire(this.details.getId(), 0);
        }

        var stats = PlayerStatisticsDao.getStatistics(this.details.getId());

        if (stats.isEmpty()) {
            PlayerStatisticsDao.newStatistics(this.details.getId(), UUID.randomUUID().toString());
            stats = PlayerStatisticsDao.getStatistics(this.details.getId());
        }

        this.statisticManager = new PlayerStatisticManager(this.details.getId(), stats);

        this.badgeManager.loadBadges(this);
        this.achievementManager.loadAchievements(this.details.getId());
        this.details.resetNextHandout();
        this.refreshJoinedGroups();

        this.send(new RIGHTS(this.getFuserights()));

        if(this.flash) {
            this.send(new UNKNOWN_290_FLASH());
        }

        this.send(new LOGIN());

        if(this.flash) {
            this.send(new RoomInfoFeed_517_FLASH());

            var homeRoom = PlayerDao.getHomeRoom(this.getDetails().getId());

            this.getDetails().setHomeRoom(homeRoom);
            this.send(new SET_HOME_ROOM_FLASH(homeRoom));

            this.send(new FAVORITE_ROOMS_FLASH(this));
        }

        this.send(new AVATAR_EFFECTS(this.effects));

        if (GameConfiguration.getInstance().getBoolean("welcome.message.enabled")) {
            String alertMessage = GameConfiguration.getInstance().getString("welcome.message.content");
            alertMessage = StringUtil.replaceAlertMessage(alertMessage, this);
            this.send(new ALERT(alertMessage));
        }

        if (PlayerManager.getInstance().isMaintenance()) {
            this.send(new INFO_HOTEL_CLOSING(PlayerManager.getInstance().getMaintenanceAt()));
        }

        /*if (this.network.isFlashConnected()) {
            String alertMessage = "Welcome to Classic Habbo! A reminder:\r\r";

            alertMessage += "This hotel exists to play all the features that were available on the Shockwave client. The flash client does not have many features.\r\r";
            alertMessage += "It is advisable to use the Shockwave client if you want to experience Classic Habbo for what it is, especially when Flash becomes deprecated on the modern browsers in December 2020.\r\r";

            alertMessage = StringUtil.replaceAlertMessage(alertMessage, this);
            this.send(new ALERT(alertMessage));
        }*/

        /*if (this.details.getXpExpiry() < DateUtil.getCurrentTimeSeconds()) {
            this.details.setXp(0);
            this.details.setXpExpiry(DateUtil.getCurrentTimeSeconds() + TimeUnit.DAYS.toSeconds(30));
            PlayerDao.saveXp(this.details.getId(), this.details.getXp(), this.details.getXpExpiry());
        }*/

        this.messenger.sendStatusUpdate();

        // Guide checks
        this.guideManager.setGuide(GuideManager.getInstance().isGuide(this));
        if (GameConfiguration.getInstance().getBoolean("tutorial.enabled")) {
            if (this.guideManager.isGuide()) {
                this.guideManager.setHasTutorial(false);
                this.guideManager.refreshGuidingUsers();
            } else {
                this.guideManager.setHasTutorial(this.statisticManager.getIntValue(PlayerStatistic.HAS_TUTORIAL) == 1);
            }
        }


        if (GameConfiguration.getInstance().getBoolean("tutorial.enabled")) {
            if (this.guideManager.hasTutorial()) {
                this.send(new HELP_ITEMS(List.of(1, 2, 3, 4, 5, 6, 7, 8)));
            }
        }

        // Guide admin
        if (this.badgeManager.hasBadge("GLK")) {
            int guideGroupId = GameConfiguration.getInstance().getInteger("guides.group.id");
            var group = this.getJoinedGroup(guideGroupId);

            if (group != null) {
                var groupMember = group.getMember(this.getDetails().getId());

                if (groupMember == null || (groupMember.getMemberRank() != GroupMemberRank.ADMINISTRATOR && groupMember.getMemberRank() != GroupMemberRank.OWNER)) {
                    this.badgeManager.removeBadge("GLK");
                }
            }
        }

        // Habbo eXperts group
        if (this.badgeManager.hasBadge("XXX")) {
            int expertsGroupId = GameConfiguration.getInstance().getInteger("habbo.experts.group.id");
            var group = this.getJoinedGroup(expertsGroupId);

            if (group != null) {
                var expertsMember = group.getMember(this.getDetails().getId());

                if (expertsMember == null) {
                    this.badgeManager.removeBadge("XXX");
                }
            }
        }

        //Country flag badge
        var countryBadges = CountryDao.getAllCountryBadges();

        var hasBadge = false;
        for(var badge : countryBadges) {
            if(this.badgeManager.hasBadge(badge)) {
                hasBadge = true;
                break;
            }
        }

        if(!hasBadge) {
            giveCountryFlagBadge();
        }

        ClubSubscription.countMemberDays(this);

        if(this.flash) {
            try {
                new SCR_GIFT_APPROVAL().handle(this, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void giveCountryFlagBadge() {

        var errorAlert = "We could not find an appropriate country badge in our database. Please contact staff";

        try {
            var address = ((InetSocketAddress)network.getChannel().remoteAddress()).getAddress().getHostAddress();

            URL url = new URL("http://ip-api.com/json/" + address);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                this.send(new ALERT(errorAlert));
                return;
            }

            // Read response body
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            var json = Havana.getGson().fromJson(response.toString(), JsonObject.class);
            var countryName = json.get("country").getAsString();

            var badge = CountryDao.getCountryBadge(countryName);

            if(badge != null && !this.badgeManager.hasBadge(badge)) {
                this.countryBadgeToGive = badge;
            }
        } catch (Exception e) {
            this.send(new ALERT(errorAlert));
        }
    }

    /**
     * Refresh club for player.
     */
    public void refreshClub() {
        if (!this.details.hasClubSubscription()) {
            // If the database still thinks we have Habbo club even after it expired, reset it back to 0.
            if (this.details.getClubExpiration() > 0) {
                this.details.setClubExpiration(0);
                this.send(new RIGHTS(this.getFuserights()));
                //ClubSubscription.resetClothes(this.details);
                PlayerDao.saveSubscription(this.details.getId(), this.details.getFirstClubSubscription(), this.details.getClubExpiration());
            }
        } else {
            ClubSubscription.checkBadges(this);

            if (ClubSubscription.isGiftDue(this)) {
                this.send(new CLUB_GIFT(this.statisticManager.getIntValue(PlayerStatistic.GIFTS_DUE)));
            }
        }

        ClubSubscription.sendHcDays(this);
    }

    public void refreshClubGiftHard() {
        if (ClubSubscription.isGiftDue(this)) {

            this.statisticManager.reload();

            if(this.flash) {
                try {
                    new SCR_GIFT_APPROVAL().handle(this, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            this.send(new CLUB_GIFT(this.statisticManager.getIntValue(PlayerStatistic.GIFTS_DUE)));
        }
    }

    /**
     * Send fuseright permissions for player.
     */
    public List<Fuseright> getFuserights() {
        List<Fuseright> fuserights = FuserightsManager.getInstance().getFuserightsForRank(this.details.getRank());

        if (this.getDetails().hasClubSubscription()) {
            fuserights.addAll(FuserightsManager.getInstance().getClubFuserights());
        }

        fuserights.removeIf(fuse -> !fuse.getFuseright().startsWith("fuse_"));
        return fuserights;
    }

    /**
     * Check if the player has a permission for a rank.
     *
     * @param fuse the permission
     * @return true, if successful
     */
    @Override
    public boolean hasFuse(Fuseright fuse) {
        return FuserightsManager.getInstance().hasFuseright(fuse, this.details);
    }

    /**
     * Send a response to the player
     *
     * @param response the response
     */
    public void send(MessageComposer response) {
        this.network.send(response);
    }

    /**
     * Send a object to the player
     *
     * @param object the object to send
     */
    public void sendObject(Object object) {
        this.network.send(object);
    }

    /**
     * Get the messenger instance for the player
     *
     * @return the messenger instance
     */
    public Messenger getMessenger() {
        return messenger;
    }

    /**
     * Get the inventory handler for player.
     *
     * @return the inventory handler
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the badge manager for player.
     *
     * @return the badge manager
     */
    public BadgeManager getBadgeManager() {
        return badgeManager;
    }

    @Override
    public PlayerDetails getDetails() {
        return this.details;
    }

    @Override
    public RoomPlayer getRoomUser() {
        return this.roomEntity;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    /**
     * Get the player logger.
     *
     * @return the logger
     */
    public Logger getLogger() {
        return this.log;
    }

    /**
     * Get the network handler for the player
     *
     * @return the network handler
     */
    public NettyPlayerNetwork getNetwork() {
        return this.network;
    }

    /**
     * Get if the player has logged in or not.
     *
     * @return true, if they have
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Get if the connection has timed out or not.
     *
     * @return false, if it hasn't.
     */
    public boolean isPingOK() {
        return pingOK;
    }

    /**
     * Get if the socket has been disconnected.
     *
     * @return true, if it has
     */
    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * Set if the connection has timed out or not.
     *
     * @param pingOK the value to determine of the connection has timed out
     */
    public void setPingOK(boolean pingOK) {
        this.pingOK = pingOK;
    }

    /**
     * Get rid of the player from the server.
     */
    public void kickFromServer() {
        try {
            this.network.send(new HOTEL_LOGOUT(LogoutReason.DISCONNECT));
            this.network.disconnect();

            this.dispose();
        } catch (Exception ignored) {
            // Ignore
        }
    }

    /**
     * Dispose player when disconnect happens.
     */
    @Override
    public void dispose() {
        if (this.loggedIn) {
            if (this.roomEntity.getRoom() != null) {
                this.roomEntity.getRoom().getEntityManager().leaveRoom(this, false);
            }

            if (this.roomEntity.getObservingGameId() != -1) {
                this.roomEntity.stopObservingGame();
            }

            if (this.roomEntity.getGamePlayer() != null) {
                this.roomEntity.getGamePlayer().getGame().leaveGame(this.roomEntity.getGamePlayer());
            }

            PlayerManager.getInstance().removePlayer(this);
            ClubSubscription.countMemberDays(this);

            int loggedInTime = (int) (DateUtil.getCurrentTimeSeconds() - this.timeConnected);
            this.statisticManager.incrementValue(PlayerStatistic.ONLINE_TIME, loggedInTime);
            this.details.setLastOnline(DateUtil.getCurrentTimeSeconds());

            GuideManager.getInstance().tryProgress(this);
            GuideManager.getInstance().checkGuidingFriends(this);

            if (!this.details.getName().equals("Abigail.Ryan")) {
                PlayerDao.saveLastOnline(this.details.getId(), this.details.getLastOnline(), false);
            }

            SettingsDao.updateSetting("players.online", String.valueOf(PlayerManager.getInstance().getPlayers().size()));

            if (this.messenger != null) {
                this.messenger.sendStatusUpdate();
            }
        }

        this.disconnected = true;
        this.loggedIn = false;
    }

    /**
     * Get the diffie-hellman instance of the user.
     *
     * @return the instance
     */
    public DiffieHellman getDiffieHellman() {
        return diffieHellman;
    }

    /**
     * Get the list of user activated effects.
     *
     * @return the list of effects
     */
    public CopyOnWriteArrayList<Effect> getEffects() {
        return effects;
    }

    /**
     * Get if the user has used the generate key
     *
     * @return true, if successful
     */
    public boolean hasGenerateKey() {
        return hasGenerateKey;
    }

    /**
     * Set whether the user has generated the key
     *
     * @param hasGenerateKey the flag
     */
    public void setHasGenerateKey(boolean hasGenerateKey) {
        this.hasGenerateKey = hasGenerateKey;
    }

    /**
     * Get the user ignored list.
     *
     * @return the user ignored list
     */
    public Set<String> getIgnoredList() {
        return ignoredList;
    }

    /**
     * Get the user achievement manager.
     *
     * @return the user achievement manager
     */
    public UserAchievementManager getAchievementManager() {
        return achievementManager;
    }

    /**
     * Get the guide manager for the user.
     *
     * @return the guide manager
     */
    public PlayerGuideManager getGuideManager() {
        return guideManager;
    }

    /**
     * Get the statistic manager for the user.
     *
     * @return the statistic manager
     */
    public PlayerStatisticManager getStatisticManager() {
        return statisticManager;
    }

    /**
     * Get whether we are processing login steps
     * @return true, if successful
     */
    public boolean isProcessLoginSteps() {
        return processLoginSteps;
    }

    /**
     * Set whether we are processing login steps
     * @param processLoginSteps the flag if we are or not
     */
    public void setProcessLoginSteps(boolean processLoginSteps) {
        this.processLoginSteps = processLoginSteps;
    }

    /**
     * Refresh the groups the user has joined
     */
    public void refreshJoinedGroups() {
        this.joinedGroups = GroupDao.getJoinedGroups(this.details.getId());
    }

    /**
     * Get the list of groups the user has joined.
     *
     * @return the list of groups
     */
    public List<Group> getJoinedGroups() {
        return joinedGroups;
    }

    /**
     * Get the joined group
     * @param joinedGroupId the joined group id
     * @return the group
     */
    public Group getJoinedGroup(int joinedGroupId) {
        return joinedGroups.stream().filter(x -> x.getId() == joinedGroupId).findFirst().orElse(null);
    }

    @Override
    public boolean isMuted() {
        //if (this.getDetails().getRank().getRankId() >= 5)
        //    return false;

        long mutedExpiresAt = this.statisticManager.getLongValue(PlayerStatistic.MUTE_EXPIRES_AT);

        if (mutedExpiresAt > 0) {
            return mutedExpiresAt > DateUtil.getCurrentTimeSeconds();
        }

        return false;
    }

    public void setLastGift(String nextSpriteGift) {
        this.lastGift = nextSpriteGift;
    }

    public String getLastGift() {
        return lastGift;
    }

    public void setHasLoadedCatalogue(boolean hasLoadedCatalogue) {
        this.hasLoadedCatalogue = hasLoadedCatalogue;
    }

    public boolean getHasLoadedCatalogue() {
        return this.hasLoadedCatalogue;
    }

    public String getCountryBadgeToGive() {
        return this.countryBadgeToGive;
    }

    public void setCountryBadgeToGive(String countryBadgeToGive) {
        this.countryBadgeToGive = countryBadgeToGive;
    }

    public boolean getHasBeenCreditsWarned() {
        return this.hasBeenCreditsWarned;
    }

    public void setHasBeenCreditsWarned(boolean b) {
        this.hasBeenCreditsWarned = b;
    }

    public boolean getHasBeenPixelsWarned() {
        return hasBeenPixelsWarned;
    }

    public void setHasBeenPixelsWarned(boolean b) {
        this.hasBeenPixelsWarned = b;
    }

    public List<String> getChatHistory() {
        return this.chatHistory;
    }

    public void addChat(String message) {
        this.chatHistory.add(message);
    }

    public void onEnterRoom() {
        chatHistory.clear();
    }
}
