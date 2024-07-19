package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingHobbasDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingHobbasController {
    public static void hobbas(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/hobbas");
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

        String finalMessage = "";
        boolean hasReasons = false;
        List<String> reasons = null;

        if (client.post().contains("userName") || client.post().contains("userID")) {
            String userName = client.post().getString("userName");
            String userID = client.post().getString("userID");

            Map<String, String> checkResults = new HashMap<>();
            String identifier = "";
            if (userName != null && !userName.isEmpty() && !StringUtils.isNumeric(userName)) {
                checkResults = HousekeepingHobbasDao.checkHabboDetails(userName); // Usar userName en lugar de userID
                identifier = " (Name: " + userName + ")";
            } else if (userID != null && !userID.isEmpty() && StringUtils.isNumeric(userID)) {
                checkResults = HousekeepingHobbasDao.checkHabboDetails(userID);
                identifier = " (ID: " + userID + ")";
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid user name or user ID");
            }

            if (!checkResults.isEmpty()) {
                hasReasons = true;
                reasons = new ArrayList<>(checkResults.values());
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "<div style=\"color:black\"><b>Checking " + identifier + "</b></div>");

                // Verificar si hay claves con _RED o valores con danger
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
            }
        }

        tpl.set("finalMessage", finalMessage);
        tpl.set("hasReasons", hasReasons);
        tpl.set("reasons", reasons);
        tpl.set("pageName", "Check Hobba applicant");
        tpl.set("hobbasFormLogs", HousekeepingHobbasDao.getLogs(currentPage));
        tpl.set("nexthobbasFormLogs", HousekeepingHobbasDao.getLogs(currentPage + 1));
        tpl.set("previoushobbasFormLogs", HousekeepingHobbasDao.getLogs(currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }
}
