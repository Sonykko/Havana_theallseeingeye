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

import java.util.HashMap;

public class HousekeepingMassAlertApiController {
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

            boolean dbInsertSuccess = HousekeepingRCONCommandsDao.insertRconLog("HOTEL_ALERT", null, moderator, message);

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
}
