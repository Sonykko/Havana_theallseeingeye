package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.GroupDiscussionDao;
import org.alexdev.http.dao.housekeeping.HousekeepingContentModerationDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.HousekeepingReportsSearchResult;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.ContentModerationUtil;

public class HousekeepingDiscussionPostReportsController {
    private static final String typeReport = "discussionpost";

    public static void discussionpost_reports(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/discussionpost_reports");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        HousekeepingReportsSearchResult searchResult = new HousekeepingReportsSearchResult();

        if (client.post().contains("latest")) {
            ContentModerationUtil.getLatestReportsUtil(tpl, searchResult, typeReport);
        }

        if (client.post().contains("searchQuery")) {
            ContentModerationUtil.search(client, tpl, searchResult, typeReport);
        }

        String objectIdsParam = client.post().getString("objectIds");
        String reportIdsParam = client.post().getString("reportIds");
        String replacementText = client.post().getString("replacementText");
        String action = client.post().getString("action");

        if (action.equals("process")) {
            process(client, playerDetails, objectIdsParam, reportIdsParam, replacementText);
        }

        tpl.set("pageName", "Discussion Post Reports");
        tpl.set("showResults", searchResult.isShowResults());
        tpl.set("totalReportsSearch", searchResult.getTotalReportsSearch());
        tpl.set("totalReports", HousekeepingContentModerationDao.countReports(typeReport));
        tpl.set("searchCriteria", searchResult.getSearchCriteria());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void process(WebConnection client, PlayerDetails playerDetails, String objectIdsParam, String reportIdsParam, String replacementText) {
        if (objectIdsParam == null || objectIdsParam.isEmpty() ||
                reportIdsParam == null || reportIdsParam.isEmpty() ||
                replacementText == null || replacementText.isEmpty()) {

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please select at least one report and provide a replacement text.");
            client.redirect(ContentModerationUtil.getPath(typeReport));
            return;
        }

        String[] objectIdsArray = objectIdsParam.split(",");
        String[] reportIdsArray = reportIdsParam.split(",");

        if (objectIdsArray.length != reportIdsArray.length) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Mismatch between selected reports and objects.");
            client.redirect(ContentModerationUtil.getPath(typeReport));
            return;
        }

        for (int i = 0; i < objectIdsArray.length; i++) {
            int objectId = Integer.parseInt(objectIdsArray[i].trim());
            int reportId = Integer.parseInt(reportIdsArray[i].trim());

            int discussionId = GroupDiscussionDao.getDiscussionId(objectId);
            int userId = GroupDiscussionDao.getReplyUserId(discussionId);
            var discussionPostMessage = GroupDiscussionDao.getReply(discussionId, objectId, userId);

            if (discussionPostMessage == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Discussion post with ID " + objectId + " does not exist.");
                client.redirect(ContentModerationUtil.getPath(typeReport));
                return;
            }

            try {
                discussionPostMessage.setMessage(replacementText);

                if (!Group.hasTopicAdmin(playerDetails.getRank())) {
                    discussionPostMessage.setEdited(true);
                }

                GroupDiscussionDao.saveReply(discussionPostMessage);
            } catch (Exception ex) {

            }

            HousekeepingContentModerationDao.setAsModerated(reportId);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Moderated " + typeReport + " content reports with the id's: " + reportIdsParam + ". URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "Discussion post reports moderated successfully.");
            client.redirect(ContentModerationUtil.getPath(typeReport));
            return;
        }
    }
}
