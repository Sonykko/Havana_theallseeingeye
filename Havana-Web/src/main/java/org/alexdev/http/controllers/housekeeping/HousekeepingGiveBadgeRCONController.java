package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRCONCommandsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.SessionUtil;

public class HousekeepingGiveBadgeRCONController {
    private static final String typeRCON = "GIVE_BADGE";

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
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
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

        String sortBy = "id";

        tpl.set("pageName", "Remote Give Badge");
        tpl.set("remoteGiveBadgesLogs", HousekeepingRCONCommandsDao.getAllTypeRCONLogs(currentPage, sortBy, typeRCON));
        tpl.set("nextremoteGiveBadgesLogs", HousekeepingRCONCommandsDao.getAllTypeRCONLogs(currentPage + 1, sortBy, typeRCON));
        tpl.set("previousremoteGiveBadgesLogs", HousekeepingRCONCommandsDao.getAllTypeRCONLogs( currentPage - 1, sortBy, typeRCON));
        tpl.set("page", currentPage);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}
