package org.alexdev.http.util.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.GroupDiscussionDao;
import org.alexdev.http.dao.GuestbookDao;
import org.alexdev.http.dao.WidgetDao;
import org.alexdev.http.dao.housekeeping.HousekeepingContentModerationDao;
import org.alexdev.http.game.housekeeping.HousekeepingContentModeration;
import org.alexdev.http.game.housekeeping.HousekeepingReportsSearchResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ContentModerationUtil {

    public static final List<HousekeepingContentModeration> getLatestReports(int limit, String typeReport) {
        String limitStrg = String.valueOf(limit);

        if (!StringUtils.isNumeric(limitStrg)) {
            limit = 20;
        }

        List<HousekeepingContentModeration> latestReports = HousekeepingContentModerationDao.searchContentReports(limit, typeReport);

        for (HousekeepingContentModeration report : latestReports) {
            getValueReport(report, typeReport);
        }

        return latestReports;
    }

    public static String getSearchCriteria(String searchType) {
        switch (searchType) {
            case "latest":
                return "latest";
            case "new":
                return "new";
            case "reported habbo":
                return "reported " + GameConfiguration.getInstance().getString("site.name").toLowerCase();
            default:
                return "latest";
        }
    }

    public static String getPath(String reportType) {
        switch (reportType) {
            case "name":
                return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/name_reports_list";
            case "motto":
                return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/motto_reports_list";
            case "guestbook":
                return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/guestbook_reports_list";
            case "groupname":
                return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/groupname_reports_list";
            case "groupdesc":
                return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/groupdesc_reports_list";
            case "discussionpost":
                return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/discussionpost_reports_list";
            default:
                return "";
        }
    }

    public static String getValueReport(HousekeepingContentModeration report, String typeReport) {
        int objectId = Integer.parseInt(report.getObjectId());
        String detail = "";

        switch (typeReport) {
            case "name":
                detail = PlayerDao.getName(objectId);
                report.setValue(detail);
                break;
            case "motto":
                detail = PlayerDao.getDetails(objectId).getMotto();
                report.setValue(detail);
                break;
            case "guestbook":
                var entry = GuestbookDao.getEntry(objectId);
                detail = entry.getMessage();
                report.setValue(detail);
                break;
            case "groupname":
                detail = GroupDao.getGroupName(objectId);
                report.setValue(detail);
                break;
            case "groupdesc":
                var groupWidget = WidgetDao.getWidget(objectId).getGroupId();
                detail = GroupDao.getGroup(groupWidget).getDescription();
                report.setValue(detail);
                break;
            case "discussionpost":
                int discussionId = GroupDiscussionDao.getDiscussionId(objectId);
                int userId = GroupDiscussionDao.getReplyUserId(discussionId);
                var discussionPostMessage = GroupDiscussionDao.getReply(discussionId, objectId, userId);
                detail = discussionPostMessage.getMessage();
                report.setValue(detail);
                break;
            default:

                break;
        }
        return detail;
    }

    public static void getLatestReportsUtil(Template tpl, HousekeepingReportsSearchResult searchResult, String typeReport) {
        List<HousekeepingContentModeration> reportsWithDetails = ContentModerationUtil.getLatestReports(20, typeReport);

        tpl.set("latestReports", reportsWithDetails);
        searchResult.setShowResults(true);
        searchResult.setTotalReportsSearch(reportsWithDetails.size());
        searchResult.setSearchCriteria(ContentModerationUtil.getSearchCriteria("latest"));
    }

    public static void search(WebConnection client, Template tpl, HousekeepingReportsSearchResult searchResult, String typeReport) {
        String reportedUser = client.post().getString("reportedUser");
        String criteria = client.post().getString("criteria");
        String showMax = client.post().getString("showMax");

        if (!StringUtils.isNumeric(criteria) || (!criteria.equals("1") && !criteria.equals("0")) || !StringUtils.isNumeric(showMax)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid criteria or show max value.");
            client.redirect(getPath(typeReport));
            return;
        }

        int criteriaInt = Integer.parseInt(criteria);
        int showMaxInt = Integer.parseInt(showMax);

        if (showMaxInt < 1 || showMaxInt > 20) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid show max value.");
            client.redirect(getPath(typeReport));
            return;
        }

        if (criteriaInt == 0) {
            List<HousekeepingContentModeration> reportsWithDetails = ContentModerationUtil.getLatestReports(showMaxInt, typeReport);


            tpl.set("latestReports", reportsWithDetails);
            searchResult.setShowResults(true);
            searchResult.setTotalReportsSearch(reportsWithDetails.size());
            searchResult.setSearchCriteria(ContentModerationUtil.getSearchCriteria("new"));
        }

        if (criteriaInt == 1) {
            if (reportedUser == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please provided a valid reported username.");
                client.redirect(getPath(typeReport));
                return;
            }

            var reportedDetails = PlayerDao.getDetails(reportedUser);

            if (reportedDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The reported does not exist.");
                client.redirect(getPath(typeReport));
                return;
            }

            List<HousekeepingContentModeration> reports = HousekeepingContentModerationDao.searchContentReports(showMaxInt, typeReport);

            List<HousekeepingContentModeration> filteredReports = reports.stream()
                    .filter(r -> {
                        String username = PlayerDao.getName(Integer.parseInt(r.getObjectId()));
                        return username != null && username.equals(reportedUser);
                    })
                    .collect(java.util.stream.Collectors.toList());

            for (HousekeepingContentModeration report : filteredReports) {
                var value = getValueReport(report, typeReport);
                report.setValue(value);
            }

            tpl.set("latestReports", filteredReports);
            searchResult.setShowResults(true);
            searchResult.setTotalReportsSearch(filteredReports.size());
            searchResult.setSearchCriteria(ContentModerationUtil.getSearchCriteria("reported habbo"));
        }
    }
}
