package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.moderation.actions.ModeratorBanUserAction;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRCONCommandsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.MessageEncoderUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HousekeepingCommandsController {

    /**
     * Handle the /housekeeping URI request
     *
     * @param client the connection
     */
    public static void superban(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        var playerDetails = PlayerDao.getDetails(client.get().getString("username"));

        if (playerDetails != null) {
            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String message = GameConfiguration.getInstance().getString("rcon.superban.message");

            if (playerDetails.getId() == banningPlayerDetails.getId()) {
                client.send("Can't superban yourself.");
                return;
            }

            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, message, "", playerDetails.getName(), 999999999, true, true));
            return;
        }

        client.send("User doesn't exist");
    }

    public static void banuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        String redirect =  client.get().getString("redirect");
        if (redirect.isEmpty()) {
            redirect = "bans_kicks";
        }

        var playerDetails = PlayerDao.getDetails(client.get().getString("username"));

        if (playerDetails != null) {
            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String alertMessage = client.get().getString("alertMessage");
            String notes = client.get().getString("notes");
            int banSeconds = client.get().getInt("banSeconds");
            boolean doBanMachine = client.get().getBoolean("doBanMachine");
            boolean doBanIP = client.get().getBoolean("doBanIP");

            if (playerDetails.getId() == banningPlayerDetails.getId()) {
                client.send("Can't ban yourself.");
                return;
            }

            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, alertMessage, notes, playerDetails.getName(), banSeconds, doBanMachine, doBanIP));

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The user " + playerDetails.getName() + " has been banned.");
            return;
        }

        client.send("User doesn't exist");
    }

    public static void massBanuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int banningId1 = client.session().getInt("user.id");
        PlayerDetails staffDetails = PlayerManager.getInstance().getPlayerData(banningId1);

        Map<String, String> params = client.get().getValues();
        String users = params.get("usernames");
        List<String> usernames = splitUsernames(users);

        for (String username : usernames) {
            String redirect =  client.get().getString("redirect");
            if (redirect.isEmpty()) {
                redirect = "mass_ban";
            }

            var playerDetails = PlayerDao.getDetails(username);

            if (playerDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user " + username + " does not exist.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
                return;
            }

            if (playerDetails.getId() == staffDetails.getId()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't mass ban yourself.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
                return;
            }

            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String alertMessage = client.get().getString("alertMessage");
            String notes = client.get().getString("notes");
            int banSeconds = client.get().getInt("banSeconds");
            boolean doBanMachine = client.get().getBoolean("doBanMachine");
            boolean doBanIP = client.get().getBoolean("doBanIP");

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, banningPlayerDetails.getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, alertMessage, notes, playerDetails.getName(), banSeconds, doBanMachine, doBanIP));

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The users " + users + " has been banned.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
            return;
        }
    }

    public static void massUnbanuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int banningId = client.session().getInt("user.id");
        PlayerDetails staffDetails = PlayerManager.getInstance().getPlayerData(banningId);

        Map<String, String> params = client.get().getValues();
        String users = params.get("usernames");
        List<String> usernames = splitUsernames(users);

        for (String username : usernames) {
            var playerDetails = PlayerDao.getDetails(username);

            if (playerDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user " + username + " does not exist.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_unban");
                return;
            }

            HousekeepingPlayerDao.unbanUser(playerDetails.getId());

            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", staffDetails.getId(), staffDetails.getName(), "Ha desbaneado al " + GameConfiguration.getInstance().getString("site.name") + " '" + username + " (id: " + playerDetails.getId() + ")'. URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The users " + users + " has been unbanned.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_unban");
            return;
        }
    }

    public static void massKickuser(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String message = client.get().getString("alertMessage");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);

        Map<String, String> params = client.get().getValues();
        String users = params.get("users");

        List<String> usernames = splitUsernames(users);

        for (String username : usernames) {
            var playerMassKickDetails = PlayerDao.getDetails(username);

            if (playerDetails.getId() == playerMassKickDetails.getId()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't mass kick yourself.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_ban");
                return;
            }

            try {
                RconUtil.sendCommand(RconHeader.MOD_KICK_USER, new HashMap<>() {{
                    put("receiver", username);
                    put("message", messageEncoded);

                }});

                //String user = client.get().getString("user");
                String moderator = playerDetails.getName();

                boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog("REMOTE_KICK", username, moderator, message);

                if (dbInsertSuccess) {
                    client.session().set("alertColour", "success");
                    client.session().set("alertMessage", "The Kick has been sent and logged in the database");
                } else {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Error inserting the kick into the database");
                }
            } catch (Exception e) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error sending the Kick: " + e.getMessage());
            }
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_ban");
    }

    public static void massalert(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String message = client.get().getString("ha");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);
        boolean showSender = client.get().getBoolean("showSender");
        String moderator = client.get().getString("user");

        try {

            RconUtil.sendCommand(RconHeader.HOTEL_ALERT, new HashMap<>() {{
                put("sender", moderator);
                put("message", messageEncoded);
                put("showSender", showSender);

            }});

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog("HOTEL_ALERT", null, moderator, message);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Hotel Alert has been sent and logged in the database");
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error inserting the alert into the database");
            }
        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending the Hotel Alert: " + e.getMessage());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_alert");
    }

    public static void kickuser(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String user = client.get().getString("user");
        String moderator = playerDetails.getName();
        String message = client.get().getString("alertMessage");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);

        var playerKickDetails = PlayerDao.getDetails(user);

        if (playerKickDetails == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The user " + user + " does not exist.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
            return;
        }

        if (playerKickDetails.getId() == playerDetails.getId()) {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "Can't kick yourself.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.MOD_KICK_USER, new HashMap<>() {{
                put("receiver", user);
                put("message", messageEncoded);

            }});

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog("REMOTE_KICK", user, moderator, message);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Kick has been sent and logged in the database");
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error inserting the kick into the database");
            }
        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending the Kick: " + e.getMessage());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
    }

    public static void alertuser(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String user = client.get().getString("user");
        String message = client.get().getString("message");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);

        try {
            RconUtil.sendCommand(RconHeader.MOD_ALERT_USER, new HashMap<>() {{
                put("receiver", user);
                put("message", messageEncoded);

            }});

            String moderator = playerDetails.getName();

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog("REMOTE_ALERT", user, moderator, message);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "Gave alert to " + user);
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error inserting the Alert into the database");
            }
        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending the Alert: " + e.getMessage());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/alert");
    }

    public static void cfhPickUp(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.CFH_PICK, new HashMap<>() {{
                put("cryId", client.get().getString("cryId"));
                put("moderator", playerDetails.getName());

            }});

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error Picking Up the CFH: " + e.getMessage());
        }

        String cryId = client.get().getString("cryId");

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_actions?cryId=" + cryId);
    }

    public static void cfhReply(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String cryIdReply = client.get().getString("cryIdReply");
        String message = client.get().getString("messageReply");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);

        try {
            RconUtil.sendCommand(RconHeader.CFH_REPLY, new HashMap<>() {{
                put("cryIdReply", cryIdReply);
                put("messageReply", messageEncoded);
                put("moderatorReply", playerDetails.getName());

            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Reply has been sent and logged in the database");

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending CFH reply: " + e.getMessage());
        }

        if (HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_logs");
        } else {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_mods");
        }
    }

    public static void cfhBlock(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.CFH_BLOCK, new HashMap<>() {{
                put("cryIdBlock", client.get().getString("cryIdBlock"));
                put("moderatorBlock", playerDetails.getName());

            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Block has been sent and logged in the database");

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending CFH block: " + e.getMessage());
        }

        if (HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_logs");
        } else {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_mods");
        }
    }

    public static void cfhFollow(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.CFH_FOLLOW, new HashMap<>() {{
                put("cryIdFollow", client.get().getString("cryIdFollow"));
                put("moderatorFollow", playerDetails.getName());

            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Follow has been sent and logged in the database");

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending CFH follow: " + e.getMessage());
        }

        if (HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_logs");
        } else {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_mods");
        }
    }

    public static void roomKick(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String moderator = playerDetails.getName();
        String roomKick = client.get().getString("roomId");
        String message = client.get().getString("alertRoomKick");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);
        String action = client.get().getString("action");
        boolean unacceptable = client.get().getBoolean("unacceptable");
        String unacceptableValue = GameConfiguration.getInstance().getString("rcon.room.unacceptable.name");
        String unacceptableDescValue = GameConfiguration.getInstance().getString("rcon.room.unacceptable.desc");
        String unacceptableAlertText = unacceptable ? "and the room has been set as Inappropriate" : "";
        boolean roomLock = client.get().getBoolean("roomLock");
        String roomLockText = roomLock ? "and closed with doorbell" : "";

        try {
            RconUtil.sendCommand(RconHeader.MOD_ROOM_KICK, new HashMap<>() {{
                //put("moderatorRoomKick", moderator);
                put("roomId", roomKick);
                put("alertRoomKick", messageEncoded);
                put("action", action);
                put("unacceptable", unacceptable);
                put("unacceptableValue", unacceptableValue);
                put("unacceptableDescValue", unacceptableDescValue);
                put("roomLock", roomLock);

            }});

            String type = action.equals("kick") ? "REMOTE_ROOM_KICK" : "REMOTE_ROOM_ALERT";

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog(type, roomKick, moderator, message);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The " + action + " has been sent and logged in the database " + unacceptableAlertText + " " + roomLockText);
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error inserting the " + action + " into the database");
            }
        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending the " + action + ": " + e.getMessage());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
    }

    public static void giveBadge(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/edit")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String user = client.get().getString("user");
        String badge = client.get().getString("badge");
        String removeBadge = "";
        boolean remove = client.get().getBoolean("remove");
        String description = "";

        if (remove) {
            removeBadge = badge;
            badge = "";
            description = "Remove badge";
        } else {
            removeBadge = "";
            description = "Add badge";
        }

        try {
            String finalBadge = badge;
            String finalRemoveBadge = removeBadge;
            RconUtil.sendCommand(RconHeader.MOD_GIVE_BADGE, new HashMap<>() {{
                put("user", user);
                put("badge", finalBadge);
                put("removeBadge", finalRemoveBadge);

            }});

            String badgeLog = finalBadge.isEmpty() ? removeBadge : finalBadge;

            boolean dbInsertSuccess = HousekeepingRCONCommandsDao.insertLog(user, badgeLog, description + ". Moderator: " + playerDetails.getName(),"GIVE_BADGE");
            //HousekeepingRCONCommandsDao.insertGiveBadgeToUser(userId, badge, 0, 0);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The badge action has been sent and logged in the database");
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error inserting the badge action into the database");
            }
        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending the badge action: " + e.getMessage());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/give_badge");
    }

    public static List<String> splitUsernames(String users) {
        String usersList = users.substring(1, users.length() - 1);

        return Arrays.stream(usersList.split(",,"))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .collect(Collectors.toList());
    }
}