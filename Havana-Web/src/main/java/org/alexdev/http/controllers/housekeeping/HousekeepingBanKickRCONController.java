package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.ModerationApiUtil;

import java.util.List;
import java.util.stream.Collectors;

public class HousekeepingBanKickRCONController {
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

            if (playerKickDetails.getId() == playerDetails.getId()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Can't kick yourself.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
                return;
            }

            if (!playerKickDetails.isOnline()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't kick the user "+ playerKickDetails.getName() + " cause it's not online");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/users/mod_tool?username=" + playerKickDetails.getName());
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/kick?user=" + playerKickDetails.getName() + "&alertMessage=" + finalMessage);
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

            if (playerBanDetails.getId() == playerDetails.getId()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't ban yourself.");
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

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/ban?username=" + playerBanDetails.getName() + "&alertMessage=" + finalMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP);
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
            usernames = usernames.stream().map(s -> ModerationApiUtil.replaceLineBreaks(s)).collect(Collectors.toList());

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
            usernames = usernames.stream().map(s -> ModerationApiUtil.replaceLineBreaks(s)).collect(Collectors.toList());
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
            usernames = usernames.stream().map(s -> ModerationApiUtil.replaceLineBreaks(s)).collect(Collectors.toList());

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
}
