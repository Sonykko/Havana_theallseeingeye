package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.dao.housekeeping.HousekeepingStickieNotesDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.MessageEncoderUtil;
import org.alexdev.http.util.housekeeping.ModerationApiUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HousekeepingStickieNotesController {
    public static void stickie_viewer(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/stickie_viewer");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String defaultStickieMessage = GameConfiguration.getInstance().getString("rcon.delete.stickie.message");
        String stickieText = "FFFF33" + defaultStickieMessage;
        String stickieTextEncoded = MessageEncoderUtil.encodeMessage(stickieText);

        boolean showResults = false;

        int totalReportsSearch = 0;

        String searchCriteria = "";

        if (client.post().contains("latest")) {
            List<Map<String, Object>> latestReports = HousekeepingStickieNotesDao.searchStickieNotes(0, 20, stickieText);
            tpl.set("latestReports", latestReports);
            showResults = true;
            totalReportsSearch = latestReports.size();
            searchCriteria = "latest";
        }

        if (client.post().contains("searchQuery")) {
            String criteria = client.post().getString("criteria");
            String username = client.post().getString("username");
            String showMax = client.post().getString("showMax");
            int showMaxInt = Integer.parseInt(showMax);
            int query = 0;
            int userId = 0;

            if (!StringUtils.isNumeric(criteria) || (!criteria.equals("1") && !criteria.equals("0"))) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid criteria.");
                client.redirect(getStickieNotesPath());
                return;
            }

            int criteriaInt = Integer.parseInt(criteria);

            if (criteriaInt == 1) {
                boolean userExists = false;
                if (!username.isEmpty()) {
                    userExists = HousekeepingPlayerDao.CheckDBName(username).equalsIgnoreCase(username);
                } else {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Please enter a valid User name.");
                    client.redirect(getStickieNotesPath());
                    return;
                }

                if (!userExists) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The user does not exist.");
                    client.redirect(getStickieNotesPath());
                    return;
                }

                var playerStickieDetails = PlayerDao.getDetails(username);
                userId = playerStickieDetails.getId();
                query = userId;
            }

            if (!StringUtils.isNumeric(showMax) || showMaxInt < 1 || showMaxInt > 20) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid show max value.");
                client.redirect(getStickieNotesPath());
                return;
            }

            if (criteriaInt == 1) {
                var playerStickieDetails = PlayerDao.getDetails(username);
                userId = playerStickieDetails.getId();
                query = userId;
                searchCriteria = "stickie owner";
            } else {
                query = 0;
                searchCriteria = "new";
            }

            List<Map<String, Object>> searchReports = HousekeepingStickieNotesDao.searchStickieNotes(query, showMaxInt, stickieText);
            tpl.set("searchReports", searchReports);
            showResults = true;
            totalReportsSearch = searchReports.size();
        }

        String action = client.post().getString("action");

        if (client.post().contains("onlyArchive")) {
            archiveStickieNote(client, playerDetails, stickieTextEncoded);
            return;
        }

        if (client.post().contains("onlyDelete")) {
            deleteStickieNote(client, playerDetails, stickieTextEncoded);
            return;
        }

        if ("massBan".equals(action)) {
            massBanStickieNoteOwner(client, playerDetails, stickieTextEncoded);
            return;
        }

        tpl.set("pageName", "Stickie Notes Reports");
        tpl.set("CFHTopics", HousekeepingCommandsDao.getCFHTopics());
        tpl.set("showResults", showResults);
        tpl.set("totalReports", HousekeepingStickieNotesDao.countStickieNotes(stickieText));
        tpl.set("totalReportsSearch", totalReportsSearch);
        tpl.set("searchCriteria", searchCriteria);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    private static String getStickieNotesPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/stickie_viewer";
    }

    public static void archiveStickieNote (WebConnection client, PlayerDetails playerDetails, String stickieTextEncoded) {
        String stickieIdsString = client.post().getString("stickieIds");
        if (stickieIdsString != null && !stickieIdsString.isEmpty()) {
            String[] stickieIds = stickieIdsString.split(",");
            for (String stickieId : stickieIds) {
                try {
                    RconUtil.sendCommand(RconHeader.MOD_STICKIE_DELETE, new HashMap<>() {{
                        put("stickieId", stickieId);
                        put("stickieText", stickieTextEncoded);
                        put("deleteStickie", false);
                    }});
                } catch (Exception e) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Error archiving Stickie Note: " + e.getMessage());
                }
            }
            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "Selected Stickie Notes have been archived successfully.");
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Archived Stickie Notes (id's: " + stickieIdsString + "). URL: " + client.request().uri(), client.getIpAddress());
        } else {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "No Stickie Notes were selected for archiving.");
        }
        client.redirect(getStickieNotesPath());
    }

    public static void deleteStickieNote (WebConnection client, PlayerDetails playerDetails, String stickieTextEncoded) {
        String stickieIdsString = client.post().getString("stickieIds");
        if (stickieIdsString != null && !stickieIdsString.isEmpty()) {
            String[] stickieIds = stickieIdsString.split(",");
            for (String stickieId : stickieIds) {
                try {
                    RconUtil.sendCommand(RconHeader.MOD_STICKIE_DELETE, new HashMap<>() {{
                        put("stickieId", stickieId);
                        put("stickieText", stickieTextEncoded);
                        put("deleteStickie", true);
                    }});
                } catch (Exception e) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Error deleting Stickie Note: " + e.getMessage());
                }
            }
            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "Selected Stickie Notes have been deleted successfully.");
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted and Archived Stickie Notes (id's: " + stickieIdsString + "). URL: " + client.request().uri(), client.getIpAddress());
        } else {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "No Stickie Notes were selected for deletion.");
        }
        client.redirect(getStickieNotesPath());
    }

    public static void massBanStickieNoteOwner (WebConnection client, PlayerDetails playerDetails, String stickieTextEncoded) {
        if (client.post().contains("userNames") && (client.post().contains("commonMessage") || client.post().contains("customMessage"))) {
            List<String> usernames = client.post().getArray("userNames");
            usernames = usernames.stream().map(s -> ModerationApiUtil.replaceLineBreaks(s)).collect(Collectors.toList());
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String notes = client.post().getString("notes");
            int banSeconds = client.post().getInt("banSeconds");
            boolean doBanMachine = client.post().getBoolean("doBanMachine");
            boolean doBanIP = client.post().getBoolean("doBanIP");

            String stickieIdsString = client.post().getString("stickieIds");
            String[] stickieIds = stickieIdsString.split(",");

            for (String stickieId : stickieIds) {
                try {
                    RconUtil.sendCommand(RconHeader.MOD_STICKIE_DELETE, new HashMap<>() {{
                        put("stickieId", stickieId);
                        put("stickieText", stickieTextEncoded);
                        put("deleteStickie", true);
                    }});
                } catch (Exception e) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Error deleting Stickie Note: " + e.getMessage());
                }
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "Selected Stickie Notes have been deleted successfully.");
                HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Banned and Deleted Stickie Notes (id's: " + stickieIdsString + "). URL: " + client.request().uri(), client.getIpAddress());
            }

            if (usernames != null && !usernames.isEmpty()) {
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_ban?usernames=" + usernames + "&alertMessage=" + alertMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP);
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid usernames");
            }
        }
    }
}