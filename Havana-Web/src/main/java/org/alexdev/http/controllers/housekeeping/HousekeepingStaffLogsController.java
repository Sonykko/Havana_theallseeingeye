package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingWordfilterDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

public class HousekeepingStaffLogsController {
    public static void permissions(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/base/permissions");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "root/login")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        String userIp = client.getIpAddress();

        tpl.set("pageName", "The All Seeing Eye");
        tpl.set("userIp", userIp);
        tpl.render();
    }

    public static void permissionslogs(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/statistics/permissions");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "configuration")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "The All Seeing Eye");
        tpl.set("PermissionLogs", HousekeepingLogsDao.getHousekeepingLogs("BAD_PERMISSIONS", currentPage));
        tpl.set("nextPermissionLogs", HousekeepingLogsDao.getHousekeepingLogs("BAD_PERMISSIONS", currentPage + 1));
        tpl.set("previousPermissionLogs", HousekeepingLogsDao.getHousekeepingLogs("BAD_PERMISSIONS", currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();
    }

    public static void staffactionlogs(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/statistics/hklogs_action");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "configuration")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "The All Seeing Eye");
        tpl.set("StaffActionLogs", HousekeepingLogsDao.getHousekeepingLogs("STAFF_ACTION", currentPage));
        tpl.set("nextStaffActionLogs", HousekeepingLogsDao.getHousekeepingLogs("STAFF_ACTION", currentPage + 1));
        tpl.set("previousStaffActionLogs", HousekeepingLogsDao.getHousekeepingLogs("STAFF_ACTION", currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();
    }

    public static void loginlogs(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/statistics/hklogs_login");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "configuration")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "The All Seeing Eye");
        tpl.set("LoginLogs", HousekeepingLogsDao.getAllLoginLogs(currentPage));
        tpl.set("nextLoginLogs", HousekeepingLogsDao.getAllLoginLogs(currentPage + 1));
        tpl.set("previousLoginLogs", HousekeepingLogsDao.getAllLoginLogs(currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();
    }
}
