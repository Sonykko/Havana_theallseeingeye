package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.moderation.actions.ModeratorBanUserAction;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HousekeepingRCONController {
    public static void massalertRCON(WebConnection client) { // Añadir BOLEAN para mostrar todos los chats chatlogs de manera predifinida o buscar por nombre, id o dueño o etc...
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/mass_alert");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH);
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

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/massalert?user=" + sender + "&ha=" + message);
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

        if (client.post().contains("user") && client.post().contains("message")) {
            String user = client.post().getString("user");
            String message = client.post().getString("message");

            if (user != null && !user.isEmpty() && message != null && !message.isEmpty()) {
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/alert?user=" + user + "&message=" + message);
                return;
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid values");
            }
        }

        //Template tpl = client.template("housekeeping/dashboard");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "User Alert");
        tpl.set("remoteAlertLogs", HousekeepingCommandsDao.RemoteAlertLogs(currentPage, sortBy));
        tpl.set("nextremoteAlertLogs", HousekeepingCommandsDao.RemoteAlertLogs(currentPage + 1, sortBy));
        tpl.set("previousremoteAlertLogs", HousekeepingCommandsDao.RemoteAlertLogs(currentPage - 1, sortBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void banKickUserRCON(WebConnection client) { // Añadir BOLEAN para mostrar todos los chats chatlogs de manera predifinida o buscar por nombre, id o dueño o etc...
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/users_modtool");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            return;
        }

        String badguy = "";
        badguy = client.get().getString("username");
        client.session().set("badguy", badguy);

        String action = client.post().getString("action");

        if ("kick".equals(action)) {
            String username = client.session().getString("badguy");
            if (username != null && !username.isEmpty()) {
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/kick?user=" + username);
                return;
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid username");
            }
        } else if ("ban".equals(action)) {
            if (client.post().contains("username") && client.post().contains("alertMessage")) {
                String username = client.session().getString("badguy");
                String alertMessage = client.post().getString("alertMessage");
                String notes = client.post().getString("notes");
                int banSeconds = client.post().getInt("banSeconds");
                boolean doBanMachine = client.post().getBoolean("doBanMachine");
                boolean doBanIP = client.post().getBoolean("doBanIP");

                if (username != null && !username.isEmpty()) {
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/ban?username=" + username + "&alertMessage=" + alertMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP);
                    return;
                } else {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Please enter a valid username");
                }
            }
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
            return;
        }

        String action = client.post().getString("action");

        if ("massKick".equals(action)) {
            List<String> usernames = client.post().getArray("userNames");
            usernames = usernames.stream().map(s -> replaceLineBreaks(s)).collect(Collectors.toList());
            if (usernames != null && !usernames.isEmpty()) {
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_kick?users=" + usernames);
                return;
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid usernames");
            }
        } else if ("massBan".equals(action)) {
            if (client.post().contains("userNames") && client.post().contains("alertMessage")) {
                List<String> usernames = client.post().getArray("userNames");
                usernames = usernames.stream().map(s -> replaceLineBreaks(s)).collect(Collectors.toList());
                String alertMessage = client.post().getString("alertMessage");
                String notes = client.post().getString("notes");
                int banSeconds = client.post().getInt("banSeconds");
                boolean doBanMachine = client.post().getBoolean("doBanMachine");
                boolean doBanIP = client.post().getBoolean("doBanIP");

                if (usernames != null && !usernames.isEmpty()) {
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_ban?usernames=" + usernames + "&alertMessage=" + alertMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP);
                    return;
                } else {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Please enter a valid usernames");
                }
            }
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
            return;
        }

        String action = client.post().getString("action");

        if ("massUnban".equals(action)) {
            if (client.post().contains("userNames")) {
                List<String> usernames = client.post().getArray("userNames");
                usernames = usernames.stream().map(s -> replaceLineBreaks(s)).collect(Collectors.toList());

                if (usernames != null && !usernames.isEmpty()) {
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_unban?usernames=" + usernames);
                    return;
                } else {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Please enter a valid usernames");
                }
            }
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Mass Unban Tool");
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static String replaceLineBreaks(String str) {
        return str.replace("\n", ",").replace("\r", ",");
    }
}