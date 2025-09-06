package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHTopicsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingMiniMailDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.minimail.MinimailMessage;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.ModerationApiUtil;

import java.util.List;
import java.util.stream.Collectors;

public class HousekeepingMiniMailController {
    public static void minimail_reports(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/minimail_reports");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        boolean showResults = false;
        int totalReportsSearch = 0;

        if (client.post().contains("searchQuery")) {
            String sender = client.post().getString("sender");
            String reporter = client.post().getString("reporter");

            if ((sender == null || sender.isEmpty()) && (reporter == null || reporter.isEmpty())) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid sender or reporter name.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/minimail_reports");
                return;
            }

            if (sender != null && !sender.isEmpty() && reporter != null && !reporter.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please search by either sender or reporter, not both.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/minimail_reports");
                return;
            }

            if (sender != null && !sender.isEmpty()) {
                var senderDetails = PlayerDao.getDetails(sender);

                if (senderDetails == null) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The sender does not exist.");
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/minimail_reports");
                    return;
                }

                List<MinimailMessage> latestReports = HousekeepingMiniMailDao.getReportedMessages(senderDetails.getId(), true);

                tpl.set("latestReports", latestReports);
                showResults = true;
                totalReportsSearch = latestReports.size();
            }

            if (reporter != null && !reporter.isEmpty()) {
                var reporterDetails = PlayerDao.getDetails(reporter);

                if (reporterDetails == null) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The reporter does not exist.");
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/minimail_reports");
                    return;
                }

                List<MinimailMessage> latestReports = HousekeepingMiniMailDao.getReportedMessages(reporterDetails.getId(), false);

                tpl.set("latestReports", latestReports);
                showResults = true;
                totalReportsSearch = latestReports.size();
            }
        }

        boolean onlyArchive = client.post().getBoolean("onlyArchive");
        boolean onlyDelete = client.post().getBoolean("onlyDelete");

        if (onlyArchive || onlyDelete) {
            archiveMiniMailReport(client, playerDetails);
            return;
        }

        String action = client.post().getString("action");

        if ("massBan".equals(action)) {
            massBanMiniMailReported(client, playerDetails);
        }

        tpl.set("pageName", "MiniMail Reports");
        tpl.set("CFHTopics", HousekeepingCFHTopicsDao.getCFHTopics());
        tpl.set("showResults", showResults);
        tpl.set("totalReportsSearch", totalReportsSearch);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void archiveMiniMailReport (WebConnection client, PlayerDetails playerDetails) {
        String messagesIdsString = client.post().getString("messagesId");
        if (messagesIdsString != null && !messagesIdsString.isEmpty()) {
            String[] messagesId = messagesIdsString.split(",");
            for (String messageId : messagesId) {
                HousekeepingMiniMailDao.archiveReportedMessage(Integer.parseInt(messageId));
                HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Archived and deleted MiniMail message (id: " + messageId + "). URL: " + client.request().uri(), client.getIpAddress());
            }
        }

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The MiniMail messages has been successfully Moderated.");
        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/minimail_reports");
    }

    public static void massBanMiniMailReported (WebConnection client, PlayerDetails playerDetails) {
        if (client.post().contains("userNames")) {
            List<String> usernames = client.post().getArray("userNames");
            usernames = usernames.stream().map(s -> ModerationApiUtil.replaceLineBreaks(s)).collect(Collectors.toList());
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.superban.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;
            String notes = client.post().getString("notes");
            int banSeconds = client.post().getInt("banSeconds");
            boolean doBanMachine = client.post().getBoolean("doBanMachine");
            boolean doBanIP = client.post().getBoolean("doBanIP");

            if (usernames == null || usernames.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid usernames");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/minimail_reports");
                return;
            }

            String messagesIdsString = client.post().getString("messagesId");
            if (messagesIdsString != null && !messagesIdsString.isEmpty()) {
                String[] messagesId = messagesIdsString.split(",");
                for (String messageId : messagesId) {
                    HousekeepingMiniMailDao.archiveReportedMessage(Integer.parseInt(messageId));
                    HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Archived and deleted MiniMail message (id: " + messageId + "). URL: " + client.request().uri(), client.getIpAddress());
                }
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/mass_ban?usernames=" + usernames + "&alertMessage=" + finalMessage + "&notes=" + notes + "&banSeconds=" + banSeconds + "&doBanMachine=" + doBanMachine + "&doBanIP=" + doBanIP + "&redirect=minimail_reports");
        }
    }
}