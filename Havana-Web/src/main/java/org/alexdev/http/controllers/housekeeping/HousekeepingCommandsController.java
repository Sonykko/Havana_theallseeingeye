package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.moderation.actions.ModeratorBanUserAction;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;

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
            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String message = GameConfiguration.getInstance().getString("rcon.superban.message");

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

        var playerDetails = PlayerDao.getDetails(client.get().getString("username"));

        if (playerDetails != null) {
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

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, alertMessage, notes, playerDetails.getName(), banSeconds, doBanMachine, doBanIP));
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The user " + banningPlayerDetails + " has been banned.");
            return;
        }

        client.send("User doesn't exist");
    }

    public static void massBanuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        Map<String, String> params = client.get().getValues();
        String users = params.get("usernames");
        List<String> usernames = splitUsernames(users);

        for (String username : usernames) {
            var playerDetails = PlayerDao.getDetails(username);

            if (playerDetails != null) {
                RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                    put("username", playerDetails.getId());
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
                client.session().set("alertMessage", "The user " + username + " has been banned.");
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user " + username + " does not exist.");
            }
        }

        // Redirect to the bans and kicks page
        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/bans_kicks");
    }

    public static void massUnbanuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        Map<String, String> params = client.get().getValues();
        String users = params.get("usernames");
        List<String> usernames = splitUsernames(users);

        for (String username : usernames) {
            var playerDetails = PlayerDao.getDetails(username);

            if (playerDetails != null) {
                HousekeepingPlayerDao.unbanUser(playerDetails.getId());

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The user " + username + " has been unbanned.");
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user " + username + " does not exist.");
            }
        }

        // Redirect to the bans and kicks page
        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_unban");
    }

    public static void massKickuser(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            return;
        }

        String message = GameConfiguration.getInstance().getString("rcon.kick.message");

        Map<String, String> params = client.get().getValues();
        String users = params.get("users");

        List<String> usernames = splitUsernames(users);

        for (String username : usernames) {
            try {
                RconUtil.sendCommand(RconHeader.MOD_KICK_USER, new HashMap<>() {{
                    put("receiver", username);
                    put("message", message);

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
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        try {

            RconUtil.sendCommand(RconHeader.HOTEL_ALERT, new HashMap<>() {{
                put("sender", client.get().getString("user"));
                put("message", client.get().getString("ha"));

            }});

            String moderator = client.get().getString("user");
            String message = client.get().getString("ha");

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog("HOTEL_ALERT", "", moderator, message);

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

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            return;
        }

        String user = client.get().getString("user");
        String moderator = playerDetails.getName();
        String message = GameConfiguration.getInstance().getString("rcon.kick.message");

        try {
            RconUtil.sendCommand(RconHeader.MOD_KICK_USER, new HashMap<>() {{
                put("receiver", user);
                put("message", message);

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

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.MOD_ALERT_USER, new HashMap<>() {{
                put("receiver", client.get().getString("user"));
                put("message", client.get().getString("message"));

            }});

            String user = client.get().getString("user");
            String moderator = playerDetails.getName();
            String message = client.get().getString("message");

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog("REMOTE_ALERT", user, moderator, message);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Alert has been sent and logged in the database");
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

    public static List<String> splitUsernames(String users) {
        String usersList = users.substring(1, users.length() - 1);

        return Arrays.stream(usersList.split(",,"))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .collect(Collectors.toList());
    }
}