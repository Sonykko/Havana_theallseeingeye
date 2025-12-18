package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.InfobusDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.infobus.InfobusPoll;
import org.alexdev.havana.game.infobus.InfobusPollData;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.DateUtil;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.HtmlUtil;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.piechart.PieChart;
import org.alexdev.http.util.piechart.Slice;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HousekeepingInfobusController {

    /**
     * Handle the /housekeeping/infobus_polls URI request
     *
     * @param client the connection
     */
    public static void polls(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/infobus_polls");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        tpl.set("pageName", "View Infobus Polls");
        tpl.set("infobusPolls", InfobusDao.getInfobusPolls());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void create_polls(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/infobus_polls_create");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            if (client.post().getValues().size() > 0) {
                String question = client.post().getString("question");

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "Infobus poll has been created successfully");

                InfobusPollData infobusPollData = new InfobusPollData(question);
                infobusPollData.getAnswers().addAll(client.post().getArray("answers[]"));
                InfobusDao.createInfobusPoll(playerDetails.getId(), infobusPollData);
                HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Created Infobus Poll titled " + question + ". URL: " + client.request().uri(), client.getIpAddress());

                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
                return;
            }
        } catch (Exception ex) {

        }

        tpl.set("pageName", "Create Infobus Poll");
        tpl.set("oneHourLater", DateUtil.getDate(DateUtil.getCurrentTimeSeconds() + TimeUnit.HOURS.toSeconds(1), "yyyy-MM-dd'T'HH:mm"));
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    /**
     * Handle the /housekeeping/infobus_polls/delete URI request
     *
     * @param client the connection
     */
    public static void delete(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/articles");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");
        InfobusPoll poll = InfobusDao.get(client.get().getInt("id"));

        if (poll == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The infobus poll does not exist");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        }

        if (poll.getInitiatedBy() != playerDetails.getId()) {
            if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus/delete_any")) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "No permission to delete other polls");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
                HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
                return;
            }
        }

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus/delete_own")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "No permission to delete");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (!client.get().contains("id")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "There was no infobus poll selected to delete");
        } else {
            var answers = InfobusDao.getAnswers(poll.getId());
            int totalAnswers = answers.values().stream().mapToInt(Integer::intValue).sum();

            if (totalAnswers > 0) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "You can't delete a poll with answers");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
                return;
            }

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "Successfully deleted the infobus poll");

            InfobusDao.delete(client.get().getInt("id"));
            InfobusDao.clearAnswers(client.get().getInt("id"));
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Deleted Infobus Poll with the ID " + client.get().getInt("id") + ". URL: " + client.request().uri(), client.getIpAddress());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");

    }

    /**
     * Handle the /housekeeping/infobus_polls/send_poll URI request
     *
     * @param client the connection
     */
    public static void send_poll(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/articles");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        InfobusPoll poll = InfobusDao.get(client.get().getInt("id"));

        if (poll == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The infobus poll does not exist");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        }

        client.session().set("alertColour", "warning");
        client.session().set("alertMessage", "The infobus poll request has been sent");

        RconUtil.sendCommand(RconHeader.INFOBUS_POLL, new HashMap<>() {{
            put("pollId", String.valueOf(poll.getId()));
        }});

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Send Infobus Poll with the ID " + poll.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");

    }

    /**
     * Handle the /housekeeping/infobus_polls/edit URI request
     *
     * @param client the connection
     */
    public static void edit(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/infobus_polls_edit");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        InfobusPoll infobusPoll = InfobusDao.get(client.get().getInt("id"));

        if (!client.get().contains("id")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "There was no infobus poll selected to edit");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        } else if (infobusPoll == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The infobus poll does not exist");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        } else {
            if (client.post().queries().size() > 0) {
                int id = infobusPoll.getId();
                String question = client.post().getString("question");

                var answers = InfobusDao.getAnswers(infobusPoll.getId());
                int totalAnswers = answers.values().stream().mapToInt(Integer::intValue).sum();

                if (totalAnswers > 0) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "You can't edit the poll if it has answers");
                } else {
                    /*var activePoll = InfobusDao.getActivePoll();

                    if (activePoll != null && activePoll.getId() != infobusPoll.getId() && enabled) {
                        client.session().set("alertColour", "warning");
                        client.session().set("alertMessage", "Cannot activate this poll while there's already a different active poll");
                        enabled = false;
                    } else {*/
                    client.session().set("alertColour", "success");
                    client.session().set("alertMessage", "The infobus poll was successfully saved");

                    InfobusPollData infobusPollData = new InfobusPollData(question);
                    infobusPollData.getAnswers().addAll(client.post().getArray("answers[]"));
                    InfobusDao.saveInfobusPoll(id, infobusPollData);
                    HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Edited Infobus Poll with the ID " + id + ". URL: " + client.request().uri(), client.getIpAddress());
                }

                //RconUtil.sendCommand(RconHeader.REFRESH_INFOBUS_POLLS, new HashMap<>());
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
                return;
            }

            //tpl.set("pollDate", DateUtil.getDate(infobusPoll.getExpiresAt(), "yyyy-MM-dd'T'HH:mm"));
            tpl.set("poll", infobusPoll);
        }

        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    /**
     * Handle the /housekeeping/infobus_polls/edit URI request
     *
     * @param client the connection
     */
    public static void view_results(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/infobus_polls_view");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        InfobusPoll infobusPoll = InfobusDao.get(client.get().getInt("id"));

        if (!client.get().contains("id")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "There was no infobus poll selected to edit");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        } else if (infobusPoll == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The infobus poll does not exist");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        } else {
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Looked the Infobus Poll results with the ID " + client.get().getInt("id") + ". URL: " + client.request().uri(), client.getIpAddress());
            tpl.set("poll", infobusPoll);
        }

        var image = new BufferedImage(500, 250, BufferedImage.TYPE_INT_ARGB);

        var answers = InfobusDao.getAnswers(infobusPoll.getId());
        int totalAnswers = answers.values().stream().mapToInt(Integer::intValue).sum();

        var slices = new ArrayList<Slice>();

        int i = 0;

        if (totalAnswers > 0) {
            for (var answer : answers.entrySet()) {
                Color color = null;

                if (i == 0) {
                    color = Color.BLUE;
                }

                if (i == 1) {
                    color = Color.RED;
                }

                if (i == 2) {
                    color = Color.YELLOW;
                }

                if (i == 3) {
                    color = Color.PINK;
                }

                if (i == 4) {
                    color = Color.ORANGE;
                }

                try {
                    slices.add(new Slice(infobusPoll.getPollData().getAnswers().get(answer.getKey()), (double) (answer.getValue() > 0 ? totalAnswers / answer.getValue() : 0), color));
                } catch (Exception ex) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "There was an answer to a question that doesn't exist, some answers may not be visible on this chart");
                }

                i++;
            }
        }

        new PieChart(image, slices);

        tpl.set("imageData", "data:image/png;base64," + HtmlUtil.encodeToString(image, "PNG"));
        tpl.set("noAnswers", totalAnswers == 0);

        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void clear_results(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/infobus_polls_view");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        InfobusPoll infobusPoll = InfobusDao.get(client.get().getInt("id"));

        if (!client.get().contains("id")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "There was no infobus poll selected to edit");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        } else if (infobusPoll == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The infobus poll does not exist");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
            return;
        } else {
            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The infobus poll has had all answers cleared");

            InfobusDao.clearAnswers(infobusPoll.getId());
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Empty the Infobus Poll answers with the ID " + infobusPoll.getId() + ". URL: " + client.request().uri(), client.getIpAddress());
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
        }
    }

    public static void close_event(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The infobus status has been sent");

        RconUtil.sendCommand(RconHeader.INFOBUS_END_EVENT, new HashMap<>());
        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Closed the Infobus event. URL: " + client.request().uri(), client.getIpAddress());
        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
    }

    public static void door_status(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "infobus")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        RconUtil.sendCommand(RconHeader.INFOBUS_DOOR_STATUS, new HashMap<>() {{
            put("doorStatus", String.valueOf(client.get().getInt("status")));
        }});

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Changed the Infobus Door Status to " + client.get().getInt("status") + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The infobus door status has been sent");

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/infobus_polls");
    }
}