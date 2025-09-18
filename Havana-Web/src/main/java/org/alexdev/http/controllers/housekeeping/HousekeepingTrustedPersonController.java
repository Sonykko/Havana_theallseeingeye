package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRankDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

public class HousekeepingTrustedPersonController {
    public static void trusted_person(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/trusted_person");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "trusted_person/manage") && playerDetails.getId() != 1) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.get().contains("revoke")) {
            revokeTrustedPerson(client, playerDetails);
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        if (client.post().contains("userName") || client.post().contains("userID")) {
            manageTrustedPerson(client, playerDetails);
        }

        tpl.set("pageName", "Trusted person tool");
        tpl.set("trustedPersons", HousekeepingPlayerDao.getTrustedPersonLogs(currentPage));
        tpl.set("nextTrustedPersons", HousekeepingPlayerDao.getTrustedPersonLogs(currentPage + 1));
        tpl.set("previousTrustedPersons", HousekeepingPlayerDao.getTrustedPersonLogs(currentPage - 1));
        tpl.set("players", HousekeepingPlayerDao.getActiveTrustedPersons());
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getTrustedPersonPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/trusted_person";
    }

    public static void revokeTrustedPerson (WebConnection client, PlayerDetails playerDetails) {
        String revoke = client.get().getString("revoke");

        var revokePlayerDetails = PlayerDao.getDetails(revoke);

        if (revokePlayerDetails == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The user does not exist");
            client.redirect(getTrustedPersonPath());
            return;
        }

        HousekeepingPlayerDao.setTrustedPerson(revoke, 0, "0");
        HousekeepingPlayerDao.logTrustedPerson(revoke, 0, playerDetails.getName(), "0");

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully revoke trusted person to user " + revoke);
        client.redirect(getTrustedPersonPath());
    }

    public static void manageTrustedPerson (WebConnection client, PlayerDetails playerDetails) {
        String userName = client.post().getString("userName");
        String userID = client.post().getString("userID");
        int userIDint = 0;
        String type = client.post().getString("type");

        if (!StringUtils.isEmpty(userID)) {
            try {
                userIDint = Integer.parseInt(userID);
            } catch (NumberFormatException e) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Invalid user ID format.");
                client.redirect(getTrustedPersonPath());
                return;
            }
        }

        if ((userName != null && !userName.isEmpty()) && (userID != null && !userID.isEmpty())) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please provide only one input: user name or user ID.");
            client.redirect(getTrustedPersonPath());
            return;
        }

        if ((userName != null && !userName.isEmpty() && userName.equalsIgnoreCase(playerDetails.getName())) ||
                (userID != null && !userID.isEmpty() && Integer.parseInt(userID) == playerDetails.getId())) {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "You can not trust-person yourself.");
            client.redirect(getTrustedPersonPath());
            return;
        }

        var userIdExists = PlayerDao.getDetails(userID);
        var userNameExists = PlayerDao.getDetails(userName);

        if (userIdExists == null && userNameExists == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The user does not exist.");
            client.redirect(getTrustedPersonPath());
            return;
        }

        HousekeepingPlayerDao.setTrustedPerson(userName, userIDint, type);
        HousekeepingPlayerDao.logTrustedPerson(userName, userIDint, playerDetails.getName(), type);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Set trusted/untrusted person to user " + (userName != null ? userName : userID) + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully set trusted person to user " + (userName != null ? userName : userID));
        client.redirect(getTrustedPersonPath());
    }
}