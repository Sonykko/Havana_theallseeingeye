package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

public class HousekeepingRanksController {
    public static void giveRank (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/give_rank");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/ranks")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().contains("user")) {
            String user = client.post().getString("user");
            String rank = client.post().getString("rankId");
            int rankId = 0;

            if (StringUtils.isNumeric(rank)) {
                rankId = Integer.parseInt(rank);
            }

            if (user.equalsIgnoreCase(String.valueOf(playerDetails.getName()))) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "You can not change the rank yourself");

                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/give_rank");

                return;
            }

            if (user.length() > 0 && rankId > 0 && rankId < 8) {
                String checkName = HousekeepingPlayerDao.CheckDBName(user);

                if (!checkName.equalsIgnoreCase(user)) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The user does not exist");
                } else {
                    HousekeepingPlayerDao.setRank(user, rankId);
                    HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Set the rank ID " + rankId + " to user " + user + ". URL: " + client.request().uri(), client.getIpAddress());

                    client.session().set("alertColour", "success");
                    client.session().set("alertMessage", "Successfully set the rank ID " + rankId + " to the user " + user);

                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/give_rank");

                    return;
                }
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid username or rank");
            }
        }

        tpl.set("pageName", "Give rank tool");
        tpl.set("allRanks", HousekeepingPlayerDao.getAllRanks());
        tpl.render();

        client.session().delete("alertMessage");
    }
}