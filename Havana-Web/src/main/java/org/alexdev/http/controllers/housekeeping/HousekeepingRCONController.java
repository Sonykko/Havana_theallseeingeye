package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRCONCommandsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class HousekeepingRCONController {
    public static void massAlertRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/mass_alert");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String sortBy = "id";

        if (client.get().contains("sort")) {
            if (client.get().getString("sort").equals("user") ||
                    client.get().getString("sort").equals("id")) {
                sortBy = client.get().getString("sort");
            }
        }

        if (client.post().contains("sender") && client.post().contains("message")) {
            String sender = playerDetails.getName();
            String message = client.post().getString("message");
            boolean showSender = client.post().getBoolean("showSender");

            if (message == null || message.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Hotel Alert message");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_alert");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/massalert?user=" + sender + "&ha=" + message + "&showSender=" + showSender);
            return;
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Hotel Alert - Mass alert");
        tpl.set("hotelAlertLogs", HousekeepingCommandsDao.MassAlertsLogs(currentPage, sortBy));
        tpl.set("nexthotelAlertLogs", HousekeepingCommandsDao.MassAlertsLogs(currentPage + 1, sortBy));
        tpl.set("previoushotelAlertLogs", HousekeepingCommandsDao.MassAlertsLogs(currentPage - 1, sortBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void alertUserRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/alert");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String sortBy = "id";

        if (client.get().contains("sort")) {
            if (client.get().getString("sort").equals("user") ||
                    client.get().getString("sort").equals("id")) {
                sortBy = client.get().getString("sort");
            }
        }

        if (client.post().contains("user")) {
            String user = client.post().getString("user");
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String message = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;

            if (user == null || user.isEmpty() || message == null || message.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please fill and enter all valid values");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/alert");
                return;
            }

            var playerAlertDetails = PlayerDao.getDetails(user);

            if (playerAlertDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user does not exist");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/alert");
                return;
            }

            if (!playerAlertDetails.isOnline()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't kick the user "+ playerAlertDetails.getName() + " cause it's not online");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/alert");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/alert?user=" + user + "&message=" + message);
            return;
        }

        //Template tpl = client.template("housekeeping/dashboard");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "User Alert");
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.set("remoteAlertLogs", HousekeepingCommandsDao.RemoteAlertLogs(currentPage, sortBy));
        tpl.set("nextremoteAlertLogs", HousekeepingCommandsDao.RemoteAlertLogs(currentPage + 1, sortBy));
        tpl.set("previousremoteAlertLogs", HousekeepingCommandsDao.RemoteAlertLogs(currentPage - 1, sortBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void banKickUserRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/users_modtool");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String badguy = "";
        badguy = client.get().getString("username");
        client.session().set("badguy", badguy);

        String action = client.post().getString("action");

        if ("kick".equals(action)) {
            String username = client.session().getString("badguy");
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.kick.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;

            if (username == null || username.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid username");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kick");
                return;
            }

            var playerKickDetails = PlayerDao.getDetails(username);

            if (playerKickDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user does not exists.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
                return;
            }

            if (!playerKickDetails.isOnline()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't kick the user "+ playerKickDetails.getName() + " cause it's not online");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/users/mod_tool?username=" + playerKickDetails.getName());
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/kick?user=" + username + "&alertMessage=" + finalMessage);
            return;
        }

        if ("ban".equals(action)) {
            String username = client.session().getString("badguy");
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.superban.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;
            String notes = client.post().getString("notes");
            int banSeconds = client.post().getInt("banSeconds");
            boolean doBanMachine = client.post().getBoolean("doBanMachine");
            boolean doBanIP = client.post().getBoolean("doBanIP");

            if (username == null || username.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid username");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
                return;
            }

            var playerBanDetails = PlayerDao.getDetails(username);

            if (playerBanDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user does not exists.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
                return;
            }

            var banDetails = playerBanDetails.isBanned();

            if (banDetails != null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user has already a active ban: \"" + banDetails.getLeft() + "\".");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/users/mod_tool?username=" + username);
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/ban?username=" + username + "&alertMessage=" + finalMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP);
            return;
        }

        if (badguy == null || badguy.isEmpty() || badguy.isBlank()) {

            tpl.set("isValidUser", false);
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid username");
        } else {
            tpl.set("isValidUser", true);
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "User Ban & Kick Tool");
        tpl.set("userBan", badguy);
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void massBanKickUserRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/mass_ban");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("massKick".equals(action)) {
            List<String> usernames = client.post().getArray("userNames");
            usernames = usernames.stream().map(s -> replaceLineBreaks(s)).collect(Collectors.toList());

            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.kick.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;

            if (usernames == null || usernames.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid usernames");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_ban");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_kick?users=" + usernames + "&alertMessage=" +  finalMessage);
            return;
        }

        if ("massBan".equals(action)) {
            List<String> usernames = client.post().getArray("userNames");
            usernames = usernames.stream().map(s -> replaceLineBreaks(s)).collect(Collectors.toList());
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.superban.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;
            String notes = client.post().getString("notes");
            int banSeconds = client.post().getInt("banSeconds");
            boolean doBanMachine = client.post().getBoolean("doBanMachine");
            boolean doBanIP = client.post().getBoolean("doBanIP");

            if (usernames == null || usernames.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid usernames");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_ban");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_ban?usernames=" + usernames + "&alertMessage=" + finalMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP);
            return;
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Mass Ban & Kick Tool");
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void massUnbanUserRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/mass_unban");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("massUnban".equals(action)) {
            List<String> usernames = client.post().getArray("userNames");
            usernames = usernames.stream().map(s -> replaceLineBreaks(s)).collect(Collectors.toList());

            if (usernames == null || usernames.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid usernames");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_unban");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_unban?usernames=" + usernames);
            return;
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Mass Unban Tool");
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void roomKickRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/room_kick");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String sortBy = "id";

        if (client.get().contains("sort")) {
            if (client.get().getString("sort").equals("user") ||
                    client.get().getString("sort").equals("id")) {
                sortBy = client.get().getString("sort");
            }
        }

        String action = client.post().getString("action");

        if (client.post().queries().size() > 0) {
            String roomId = client.post().getString("roomId");
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.kick.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;
            boolean unacceptable = client.post().getBoolean("unacceptable");
            boolean roomLock = client.post().getBoolean("roomLock");

            if (roomId.length() > 19 || roomId.isEmpty() || !StringUtils.isNumeric(roomId)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid room ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
                return;
            }

            long roomIdInt = Long.parseLong(roomId);

            Room room = RoomDao.getRoomById((int) roomIdInt);

            if (room == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The room not exist");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
                return;
            }

            if (room.isPublicRoom()) {
                unacceptable = false;
                roomLock = false;
            }

            String actionType = null;

            if ("roomKick".equals(action)) {
                actionType = "kick";
            } else if ("roomAlert".equals(action)) {
                actionType = "alert";
            }

            if (actionType.equals("alert") && finalMessage.equals(defaultMessage)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid alert message");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/room.kick?roomId=" + roomId + "&alertRoomKick=" + finalMessage + "&action=" + actionType + "&unacceptable=" + unacceptable + "&roomLock=" + roomLock);
            return;

        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Remote room alerting & kicking");
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.set("remoteRoomKickLogs", HousekeepingCommandsDao.RemoteRoomKicksLogs(currentPage, sortBy));
        tpl.set("nextremoteRoomKickLogs", HousekeepingCommandsDao.RemoteRoomKicksLogs(currentPage + 1, sortBy));
        tpl.set("previousremoteRoomKickLogs", HousekeepingCommandsDao.RemoteRoomKicksLogs(currentPage - 1, sortBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void giveBadge(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/give_badge");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/edit")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String action = client.post().getString("action");

        if (client.post().contains("user") && client.post().contains("badge")) {
            String user = client.post().getString("user");
            String badge = client.post().getString("badge");
            boolean remove = false;
            var playerDetailsBadge = PlayerDao.getDetails(user);

            if ("giveBadge".equals(action)) {
                remove = false;
            }

            if ("removeBadge".equals(action)) {
                remove = true;
            }

            if (badge.isEmpty() || action == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid badge code or action");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/give_badge");
                return;
            }

            if (playerDetailsBadge == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid username");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/give_badge");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/give.badge?user=" + user + "&badge=" + badge + "&remove=" + remove);
            return;
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Remote Give Badge");
        tpl.set("remoteGiveBadgesLogs", HousekeepingRCONCommandsDao.getLogs(currentPage, "GIVE_BADGE"));
        tpl.set("nextremoteGiveBadgesLogs", HousekeepingRCONCommandsDao.getLogs(currentPage + 1, "GIVE_BADGE"));
        tpl.set("previousremoteGiveBadgesLogs", HousekeepingRCONCommandsDao.getLogs(currentPage - 1, "GIVE_BADGE"));
        tpl.set("page", currentPage);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static String replaceLineBreaks(String str) {
        return str.replace("\n", ",").replace("\r", ",");
    }
}