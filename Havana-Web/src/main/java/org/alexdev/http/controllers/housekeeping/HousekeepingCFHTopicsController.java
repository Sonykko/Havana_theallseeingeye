package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHTopicsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingCFHTopics;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;

public class HousekeepingCFHTopicsController {
    public static void cfh_topics (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/cfh_topics");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "cfh/topics")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("createTopic".equals(action)) {
            createTopic(client, playerDetails);
            return;
        }

        if ("searchTopic".equals(action)) {
            searchTopic(client, tpl);
        }

        if ("topicSave".equals(action)) {
            saveTopic(client, playerDetails);
            return;
        }

        if ("topicDelete".equals(action)) {
            deleteTopic(client, playerDetails);
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "CFH topics tool");
        tpl.set("CFHTopics", HousekeepingCFHTopicsDao.getCFHTopicsByPage(currentPage));
        tpl.set("nextTopics", HousekeepingCFHTopicsDao.getCFHTopicsByPage(currentPage + 1));
        tpl.set("previousTopics", HousekeepingCFHTopicsDao.getCFHTopicsByPage(currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getCFHTopicsPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_topics";
    }
    private static void refreshCFHTopics() {
        RconUtil.sendCommand(RconHeader.REFRESH_CFH_TOPICS, new HashMap<>() {});
    }

    private static void createTopic(WebConnection client, PlayerDetails playerDetails) {
        String sanctionReasonId = client.post().getString("sanctionReasonId");
        String sanctionReasonValue = client.post().getString("sanctionReasonValue");
        String sanctionReasonDesc = client.post().getString("sanctionReasonDesc");

        if (sanctionReasonId.isEmpty() || sanctionReasonValue.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please fill a valid reason id and value");
            client.redirect(getCFHTopicsPath());
            return;
        }

        if (sanctionReasonId.length() > 255 || sanctionReasonValue.length() > 255 || sanctionReasonDesc.length() > 255) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The values are too long");
            client.redirect(getCFHTopicsPath());
            return;
        }

        HousekeepingCFHTopicsDao.create(sanctionReasonId, sanctionReasonValue, sanctionReasonDesc);
        refreshCFHTopics();

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Created CFH topic. URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully created CFH topic");
        client.redirect(getCFHTopicsPath());
    }

    private static void searchTopic(WebConnection client, Template tpl) {
        String query = client.post().getString("searchStr");

        if (StringUtils.isEmpty(query)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid search value");
            client.redirect(getCFHTopicsPath());
            return;
        }

        List<HousekeepingCFHTopics> searchTopics = HousekeepingCFHTopicsDao.searchTopics(query);

        tpl.set("query", query);
        tpl.set("searchTopicsDetails", searchTopics);
        tpl.set("searchEmpty", searchTopics.isEmpty());
    }

    private static void saveTopic(WebConnection client, PlayerDetails playerDetails) {
        String topicIdStr = client.post().getString("topicId");
        int topicId = !NumberUtils.isParsable(topicIdStr) ? 0 : Integer.parseInt(topicIdStr);
        String sanctionReasonId = client.post().getString("sanctionReasonId");
        String sanctionReasonValue = client.post().getString("sanctionReasonValue");
        String sanctionReasonDesc = client.post().getString("sanctionReasonDesc");

        var topic = HousekeepingCFHTopicsDao.getCFHTopicById(topicId);

        if (topic == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't save the topic cause not exists");
            client.redirect(getCFHTopicsPath());
            return;
        }

        if (sanctionReasonId.isEmpty() || sanctionReasonValue.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please fill a valid reason id and value");
            client.redirect(getCFHTopicsPath());
            return;
        }

        if (sanctionReasonId.length() > 255 || sanctionReasonValue.length() > 255 || sanctionReasonDesc.length() > 255) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The values are too long");
            client.redirect(getCFHTopicsPath());
            return;
        }

        topic.setSanctionReasonId(sanctionReasonId);
        topic.setSanctionReasonValue(sanctionReasonValue);
        topic.setSanctionReasonDesc(sanctionReasonDesc);

        HousekeepingCFHTopicsDao.save(topic);
        refreshCFHTopics();

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Updated the CFH topic with the ID " + topic.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the CFH topic with the ID " + topic.getId());
        client.redirect(getCFHTopicsPath());
    }

    private static void deleteTopic(WebConnection client, PlayerDetails playerDetails) {
        String topicIdStr = client.post().getString("topicId");
        int topicId = !NumberUtils.isParsable(topicIdStr) ? 0 : Integer.parseInt(topicIdStr);

        var topic = HousekeepingCFHTopicsDao.getCFHTopicById(topicId);

        if (topic == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't delete the topic cause not exists");
            client.redirect(getCFHTopicsPath());
            return;
        }

        HousekeepingCFHTopicsDao.delete(topic.getId());
        refreshCFHTopics();

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Deleted the CFH topic with the ID " + topic.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully deleted the CFH topic");
        client.redirect(getCFHTopicsPath());
    }
}