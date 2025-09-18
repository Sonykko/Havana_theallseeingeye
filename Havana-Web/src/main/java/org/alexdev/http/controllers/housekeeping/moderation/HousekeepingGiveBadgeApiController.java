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

import java.util.HashMap;

public class HousekeepingGiveBadgeApiController {
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

            boolean dbInsertSuccess = HousekeepingRCONCommandsDao.insertRconLog("GIVE_BADGE", user, badgeLog, description + ". Moderator: " + playerDetails.getName());

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
}
