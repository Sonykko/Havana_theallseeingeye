package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRankDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.HousekeepingRankVar;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.MessageEncoderUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

public class HousekeepingRanksController {
    public static void giveRank (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/give_rank");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/ranks") && playerDetails.getId() != 1) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("giveRank".equals(action)) {
            giveRankToUser(client, playerDetails);
            return;
        }

        if ("staffVars".equals(action)) {
            editStaffVars(client, playerDetails);
            return;
        }

        List<PlayerDetails> allStaffDetails = HousekeepingRankDao.getAllStaffsNames();

        tpl.set("pageName", "Give rank tool");
        tpl.set("allRanks", HousekeepingRankDao.getAllRanksVars());
        tpl.set("staffDetailsList", allStaffDetails);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getGiveRankPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/give_rank";
    }
    
    public static void giveRankToUser (WebConnection client, PlayerDetails playerDetails) {
        String user = client.post().getString("user");
        String rank = client.post().getString("rankId");
        boolean sendAlert = client.post().getBoolean("sendAlert");
        int rankId = 0;

        if (StringUtils.isNumeric(rank)) {
            rankId = Integer.parseInt(rank);
        }

        if (user.equalsIgnoreCase(String.valueOf(playerDetails.getName()))) {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "You can not change the rank yourself");
            client.redirect(getGiveRankPath());
            return;
        }

        if (user.isEmpty() || rankId < 1 || rankId > 8) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid username or rank");
            client.redirect(getGiveRankPath());
            return;
        }

        var playerDetailsRank = PlayerDao.getDetails(user);

        if (playerDetailsRank == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The user does not exist");
            client.redirect(getGiveRankPath());
            return;
        }

        var rankName = playerDetailsRank.getRankName();

        if (playerDetailsRank.getRank().getRankId() == rankId) {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "The user " + user + " already has the rank " + rankName + " (id: " + rankId + ")");
            client.redirect(getGiveRankPath());
            return;
        }

        List<HousekeepingRankVar> allRanks = HousekeepingRankDao.getAllRanksVars();

        String currentBadge = "";

        for (HousekeepingRankVar rankDetails : allRanks) {
            if (rankDetails.getId() == playerDetailsRank.getRank().getRankId()) {
                currentBadge = rankDetails.getBadge();
                break;
            }
        }

        String newBadge = "";
        for (HousekeepingRankVar rankDetails : allRanks) {
            if (rankDetails.getId() == rankId) {
                newBadge = rankDetails.getBadge();
                break;
            }
        }

        if (currentBadge != null && !currentBadge.isEmpty()) {
            String finalCurrentBadge = currentBadge;
            String finalNewBadge = newBadge;
            RconUtil.sendCommand(RconHeader.MOD_GIVE_BADGE, new HashMap<>() {{
                put("user", user);
                put("removeBadge", finalCurrentBadge);
                put("badge", finalNewBadge);

            }});
        }

        HousekeepingPlayerDao.setRank(user, rankId);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Set the rank ID " + rankId + " (" + rankName + ") to user " + user + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully set the rank ID " + rankId + " (" + rankName + ") to the user " + user);

        client.redirect(getGiveRankPath());

        if (sendAlert) {
            String message = GameConfiguration.getInstance().getString("rcon.give.rank.message");
            String finalMessage = StringUtils.replace(message, "%rank%", rankName);
            String messageEncoded = MessageEncoderUtil.encodeMessage(finalMessage);

            RconUtil.sendCommand(RconHeader.MOD_ALERT_USER, new HashMap<>() {{
                put("receiver", user);
                put("message", messageEncoded);

            }});
        }
    }
    
    public static void editStaffVars (WebConnection client, PlayerDetails playerDetails) {
        String rankId = client.post().getString("rankIdVars");
        String rankName = client.post().getString("rankNameVars");
        String rankBadge = client.post().getString("rankBadgeVars");
        String rankDescription = client.post().getString("rankDescVars");

        int rankIdInt = StringUtils.isNumeric(rankId) ? Integer.parseInt(rankId) : 0;

        if (rankIdInt < 1 || rankIdInt > 8) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid rank ID in the staff texts variables");
            client.redirect(getGiveRankPath());
            return;
        }

        var rankVar = HousekeepingRankDao.getRankVarByRankId(rankIdInt);

        if (rankVar == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The rank does not exist");
            client.redirect(getGiveRankPath());
            return;
        }

        if (rankName.isEmpty() || rankBadge.isEmpty() || rankDescription.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please fill all the texts variables for rank " + rankVar.getName() + " (id: " + rankVar.getId() + ")");
            client.redirect(getGiveRankPath());
            return;
        }

        rankVar.setName(rankName);
        rankVar.setBadge(rankBadge);
        rankVar.setDescription(rankDescription);

        HousekeepingPlayerDao.setRankTextVars(rankVar.getId(), rankVar.getName(), rankVar.getBadge(), rankVar.getDescription());
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Updated the variables for rank " + rankVar.getName() + " (id: " + rankVar.getId() + "). URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the texts variables for rank " + rankVar.getName() + " (id: " + rankVar.getId() + ")");

        client.redirect(getGiveRankPath());
    }
}