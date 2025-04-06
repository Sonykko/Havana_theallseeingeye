package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingHobbasDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HousekeepingHobbasController {
    public static void hobbas_check(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/hobbas_check");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("check".equals(action)) {
            checkUserForBeHobba(client, tpl);
        }

        tpl.set("pageName", "Check Hobba applicant");
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getHobbaCheckPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/hobbas/check";
    }

    public static void checkUserForBeHobba (WebConnection client, Template tpl) {
        String finalMessage = "";
        boolean hasReasons = false;
        List<String> reasons = null;

        String userName = client.post().getString("userName");
        String userID = client.post().getString("userID");

        if (userName.isEmpty() && userID.isEmpty() || !userName.isEmpty() && !userID.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid user name or ID");
            client.redirect(getHobbaCheckPath());
            return;
        }

        PlayerDetails userDetails;
        if (StringUtils.isNumeric(userID) && userName.isEmpty()) {
            userDetails = PlayerDao.getDetails(Integer.parseInt(userID));
        } else {
            userDetails = PlayerDao.getDetails(userName);
        }

        if (userDetails == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The user does not exist");
            client.redirect(getHobbaCheckPath());
            return;
        }

        String identifier = userDetails.getName() + " (" + userDetails.getId() + ")";
        Map<String, String> checkResults = HousekeepingHobbasDao.checkHabboDetails(userDetails.getId());
        hasReasons = true;
        reasons = new ArrayList<>(checkResults.values());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "<div style=\"color:black\"><b>Checking " + identifier + "</b></div>");

        boolean containsRedKey = false;
        boolean containsDangerValue = false;

        for (Map.Entry<String, String> entry : checkResults.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.contains("_RED")) {
                containsRedKey = true;
            }
            if (value.contains("danger")) {
                containsDangerValue = true;
            }
        }

        if (containsRedKey || containsDangerValue) {
            finalMessage = "<b>The user is not qualified to become a Hobba.</b>";
        } else {
            finalMessage = "<b>The user is qualified to become a Hobba.</b>";
        }

        tpl.set("finalMessage", finalMessage);
        tpl.set("hasReasons", hasReasons);
        tpl.set("reasons", reasons);
    }

    public static void hobbas_applications(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/hobbas_applications");
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

        if (client.post().contains("logId")) {
            pickUpHobbaForm(client, playerDetails);
            return;
        }

        tpl.set("pageName", "Hobba applications");
        tpl.set("hobbasFormLogs", HousekeepingHobbasDao.getLogs(currentPage));
        tpl.set("nexthobbasFormLogs", HousekeepingHobbasDao.getLogs(currentPage + 1));
        tpl.set("previoushobbasFormLogs", HousekeepingHobbasDao.getLogs(currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getHobbaApplicationsPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/hobbas/applications";
    }

    public static void pickUpHobbaForm (WebConnection client, PlayerDetails playerDetails) {
        String logId = client.post().getString("logId");

        if (!StringUtils.isNumeric(logId) && Integer.parseInt(logId) < 0) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hobba application form ID");
            client.redirect(getHobbaApplicationsPath());
            return;
        }

        HousekeepingHobbasDao.updateApplication(Integer.parseInt(logId));

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Has picked up the Hobba application with the ID: " + logId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Hobba application has been picked up");
        client.redirect(getHobbaApplicationsPath());
    }
}