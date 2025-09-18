package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHTopicsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRCONCommandsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

public class HousekeepingAlertRCONController {
    private static final String typeRCON = "REMOTE_ALERT";

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
                client.session().set("alertMessage", "Can't alert the user "+ playerAlertDetails.getName() + " cause it's not online");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/alert");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/alert?user=" + user + "&message=" + message);
            return;
        }

        tpl.set("pageName", "User Alert");
        tpl.set("CFHTopics", HousekeepingCFHTopicsDao.getCFHTopics());
        tpl.set("remoteAlertLogs", HousekeepingRCONCommandsDao.getAllTypeRCONLogs(currentPage, sortBy, typeRCON));
        tpl.set("nextremoteAlertLogs", HousekeepingRCONCommandsDao.getAllTypeRCONLogs(currentPage + 1, sortBy, typeRCON));
        tpl.set("previousremoteAlertLogs", HousekeepingRCONCommandsDao.getAllTypeRCONLogs(currentPage - 1, sortBy, typeRCON));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}
