package org.alexdev.http.controllers.housekeeping.moderation;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRCONCommandsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.MessageEncoderUtil;
import org.alexdev.http.util.housekeeping.ModerationApiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingKickApiController {
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
            client.send("Player does not exist");
            return;
        }

        if (playerKickDetails.getId() == playerDetails.getId()) {
            client.send("Can't kick yourself");
            return;
        }

        if (!playerKickDetails.isOnline()) {
            client.send("Player not online");
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.MOD_KICK_USER, new HashMap<>() {{
                put("receiver", user);
                put("message", messageEncoded);

            }});

            boolean dbInsertSuccess = HousekeepingRCONCommandsDao.insertRconLog("REMOTE_KICK", user, moderator, message);

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

        if (!users.startsWith("[") && !users.endsWith("]") || users.equals("[]")) {
            client.send("Invalid params");
            return;
        }

        List<String> usernames = ModerationApiUtil.splitUsernames(users);

        for (String username : usernames) {
            var playerMassKickDetails = PlayerDao.getDetails(username);

            if (playerMassKickDetails == null) {
                client.send("Player does not exist");
                return;
            }

            if (playerMassKickDetails.getId() == playerDetails.getId()) {
                client.send("Can't kick yourself");
                return;
            }

            if (!playerMassKickDetails.isOnline()) {
                client.send("Player not online");
                return;
            }

            try {
                RconUtil.sendCommand(RconHeader.MOD_KICK_USER, new HashMap<>() {{
                    put("receiver", username);
                    put("message", messageEncoded);

                }});

                //String user = client.get().getString("user");
                String moderator = playerDetails.getName();

                boolean dbInsertSuccess = HousekeepingRCONCommandsDao.insertRconLog("REMOTE_KICK", username, moderator, message);

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
}
