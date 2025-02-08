package org.alexdev.http.util.housekeeping;

import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.dao.GroupDiscussionDao;
import org.alexdev.http.dao.GuestbookDao;
import org.alexdev.http.dao.WidgetDao;
import org.alexdev.http.dao.housekeeping.HousekeepingContentModerationDao;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentModerationUtil {

    public static final List<Map<String, Object>> getLatestReports(int limit, String typeReport) {
        String limitStrg = String.valueOf(limit);

        if (!StringUtils.isNumeric(limitStrg)) {
            limit = 20;
        }

        List<Map<String, Object>> latestReports = HousekeepingContentModerationDao.searchContentReports(limit, typeReport);

        List<Map<String, Object>> reportsWithDetails = new ArrayList<>();

        for (Map<String, Object> report : latestReports) {
            int objectId = (int) report.get("objectId");
            String detail = "";

            switch (typeReport) {
                case "name":
                    detail = PlayerDao.getName(objectId);
                    report.put("name", detail);
                    break;
                case "motto":
                    detail = PlayerDao.getDetails(objectId).getMotto();
                    report.put("motto", detail);
                    break;
                case "guestbook":
                    var entry = GuestbookDao.getEntry(objectId);
                    detail = entry.getMessage();
                    report.put("guestbookEntry", detail);
                    break;
                case "groupname":
                    detail = GroupDao.getGroupName(objectId);
                    report.put("groupName", detail);
                    break;
                case "groupdesc":
                    var groupWidget = WidgetDao.getWidget(objectId).getGroupId();
                    detail = GroupDao.getGroup(groupWidget).getDescription();
                    report.put("groupDesc", detail);
                    break;
                case "discussionpost":
                    int discussionId = GroupDiscussionDao.getDiscussionId(objectId);
                    int userId = GroupDiscussionDao.getReplyUserId(discussionId);
                    var discussionPostMessage = GroupDiscussionDao.getReply(discussionId, objectId, userId);
                    detail = discussionPostMessage.getMessage();
                    report.put("postMessage", detail);
                    break;
                default:

                    break;
            }

            reportsWithDetails.add(report);
        }

        return reportsWithDetails;
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
}