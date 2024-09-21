package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingWordfilterDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingWordfilterController {
    public static void wordfilter (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/wordfilter");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().contains("addword")) {
            String addword = client.post().getString("addword");
            String isBannable = client.post().getString("isBannable");
            String isFilterable = client.post().getString("isFilterable");

            if (addword.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid word");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            boolean wordExists = HousekeepingWordfilterDao.CheckWord(addword);

            if (wordExists) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The word already exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            if (!isBannable.equals("0") && !isBannable.equals("1") || !isFilterable.equals("0") && !isFilterable.equals("1")) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Enter a valid bannable or filtrable value");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            int isBannableInt = client.post().getInt("isBannable");
            int isFilterableInt = client.post().getInt("isFilterable");

            HousekeepingWordfilterDao.createWord(addword, isBannableInt, isFilterableInt);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Added the word " + addword + " to Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

            RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {{
            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The word has been successfully added to wordfilter list");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
            return;
        }

        if (client.get().contains("delete")) {
            String wordId = client.get().getString("delete");

            if (!StringUtils.isNumeric(wordId)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid word ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            int wordIdInt = Integer.parseInt(wordId);
            List<Map<String, Object>> checkWord = HousekeepingWordfilterDao.editWord(wordIdInt);

            if (checkWord.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The word ID not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            HousekeepingWordfilterDao.deleteWord(wordIdInt);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted word with the ID " + wordId + " of Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

            RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {{
            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The word has been successfully deleted from Wordfilter");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
            return;
        }

        if (client.get().contains("edit")) {
            tpl.set("wordEdit", HousekeepingWordfilterDao.editWord(client.get().getInt("edit")));
            tpl.set("isWordEdit", true);
        } else {
            tpl.set("isWordEdit", false);
        }

        if (client.post().contains("saveWord")) {
            String saveWord = client.post().getString("saveWord");
            String isBannable = client.post().getString("isBannable");
            String isFilterable = client.post().getString("isFilterable");

            if (!StringUtils.isNumeric(client.get().getString("edit"))) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid word ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            int wordId = client.get().getInt("edit");

            if (client.post().getString("saveWord").isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid word");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter?edit=" + wordId);
                return;
            }

            if (!isBannable.equals("0") && !isBannable.equals("1") || !isFilterable.equals("0") && !isFilterable.equals("1")) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Enter a valid bannable or filtrable value");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
                return;
            }

            int isBannableInt = client.post().getInt("isBannable");
            int isFilterableInt = client.post().getInt("isFilterable");

            List<Map<String, Object>> wordEdit = HousekeepingWordfilterDao.editWord(wordId);
            String currentWord = wordEdit.get(0).get("wordFilter").toString();

            if (!saveWord.equalsIgnoreCase(currentWord)) {
                boolean wordExists = HousekeepingWordfilterDao.CheckWord(saveWord);

                if (wordExists) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The word already exists");
                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter?edit=" + wordId);
                    return;
                }
            }

            HousekeepingWordfilterDao.saveWord(saveWord, isBannableInt, isFilterableInt, wordId);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited the word " + saveWord + " of Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

            RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {{
            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The word has been successfully saved");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter");
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String sortBy = "id";
        String orderBy = "DESC";

        if (client.get().contains("sort")) {
            if (client.get().getString("sort").equals("word") ||
                    client.get().getString("sort").equals("id")) {
                sortBy = client.get().getString("sort");
            }
            if (client.get().getString("sort").equals("word")) {
                orderBy = "ASC";
            }
        }

        tpl.set("pageName", "Wordfilter");
        tpl.set("Words", HousekeepingWordfilterDao.getAllWords(currentPage, sortBy, orderBy));
        tpl.set("nextWords", HousekeepingWordfilterDao.getAllWords(currentPage + 1, sortBy, orderBy));
        tpl.set("previousWords", HousekeepingWordfilterDao.getAllWords(currentPage - 1, sortBy, orderBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.set("orderBy", orderBy);
        tpl.render();

        client.session().delete("alertMessage");
    }
}