package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

public class HousekeepingMassAlertRCONController {
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
}
