package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingContentModerationDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.ContentModerationUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HousekeepingNameReportsController {
    public static void name_reports(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/name_reports");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        boolean showResults = false;
        int totalReportsSearch = 0;
        String searchCriteria = "";
        String typeReport = "name";

        if (client.post().contains("latest")) {
            List<Map<String, Object>> reportsWithDetails = ContentModerationUtil.getLatestReports(20, typeReport);

            tpl.set("latestReports", reportsWithDetails);
            showResults = true;
            totalReportsSearch = reportsWithDetails.size();
            searchCriteria = ContentModerationUtil.getSearchCriteria("latest");
        }

        if (client.post().contains("searchQuery")) {
            String reportedUser = client.post().getString("reportedUser");
            String criteria = client.post().getString("criteria");
            String showMax = client.post().getString("showMax");

            if (!StringUtils.isNumeric(criteria) || (!criteria.equals("1") && !criteria.equals("0")) || !StringUtils.isNumeric(showMax)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid criteria or show max value.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                return;
            }

            int criteriaInt = Integer.parseInt(criteria);
            int showMaxInt = Integer.parseInt(showMax);

            if (showMaxInt < 1 || showMaxInt > 20) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid show max value.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                return;
            }

            if (criteriaInt == 0) {
                List<Map<String, Object>> reportsWithDetails = ContentModerationUtil.getLatestReports(showMaxInt, typeReport);

                tpl.set("latestReports", reportsWithDetails);
                showResults = true;
                totalReportsSearch = reportsWithDetails.size();
                searchCriteria = ContentModerationUtil.getSearchCriteria("new");
            }

            if (criteriaInt == 1) {
                if (reportedUser == null) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Please provided a valid reported username.");
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                    return;
                }

                var reportedDetails = PlayerDao.getDetails(reportedUser);

                if (reportedDetails == null) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The reported does not exist.");
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                    return;
                }

                List<Map<String, Object>> latestReports = HousekeepingContentModerationDao.searchContentReports(showMaxInt, typeReport);

                List<Map<String, Object>> filteredReports = latestReports.stream()
                        .filter(report -> {
                            int objectId = (int) report.get("objectId");
                            String username = PlayerDao.getName(objectId);
                            if (username == null){
                                username = "";
                            }
                            return username.equals(reportedUser);
                        })
                        .collect(Collectors.toList());

                List<Map<String, Object>> updatedReports = new ArrayList<>();

                for (Map<String, Object> report : filteredReports) {
                    int objectId = (int) report.get("objectId");
                    String name = PlayerDao.getName(objectId);

                    report.put("name", name);

                    updatedReports.add(report);
                }

                tpl.set("latestReports", updatedReports);
                showResults = true;
                totalReportsSearch = updatedReports.size();
                searchCriteria = ContentModerationUtil.getSearchCriteria("reported habbo");
            }
        }

        String objectIdsParam = client.post().getString("objectIds");
        String reportIdsParam = client.post().getString("reportIds");
        String action = client.post().getString("action");

        if (action.equals("process")) {
            if (objectIdsParam == null || objectIdsParam.isEmpty() || reportIdsParam == null || reportIdsParam.isEmpty()) {

                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please select at least one report.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                return;
            }

            String[] objectIdsArray = objectIdsParam.split(",");
            String[] reportIdsArray = reportIdsParam.split(",");

            if (objectIdsArray.length != reportIdsArray.length) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Mismatch between selected reports and objects.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                return;
            }

            for (int i = 0; i < objectIdsArray.length; i++) {
                int objectId = Integer.parseInt(objectIdsArray[i].trim());
                int reportId = Integer.parseInt(reportIdsArray[i].trim());

                var nameReported = PlayerDao.getDetails(objectId);

                if (nameReported == null) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The user with the ID " + objectId + " does not exist.");
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list");
                    return;
                }

                String replacementText = client.post().getString("replacementText");
                String defaultMessage = GameConfiguration.getInstance().getString("rcon.superban.message");
                String finalMessage = replacementText.isEmpty() ? defaultMessage : replacementText;

                HousekeepingContentModerationDao.setAsModerated(reportId);
                HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Moderated " + typeReport + " content reports with the id's: " + reportIdsParam + ". URL: " + client.request().uri(), client.getIpAddress());

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "Name reports moderated successfully.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/ban?username=" + nameReported.getName() + "&alertMessage=" + finalMessage + "&notes=" + "Automatic name reported ban" + "&banSeconds=360100800&doBanMachine=false&doBanIP=false&redirect=name_reports_list");
                return;
            }
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());
        tpl.set("pageName", GameConfiguration.getInstance().getString("site.name") + " Name Reports");
        tpl.set("showResults", showResults);
        tpl.set("totalReportsSearch", totalReportsSearch);
        tpl.set("totalReports", HousekeepingContentModerationDao.countReports(typeReport));
        tpl.set("searchCriteria", searchCriteria);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}
