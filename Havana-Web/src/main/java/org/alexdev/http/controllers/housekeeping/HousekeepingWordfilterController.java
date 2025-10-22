package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingWordfilterDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.HousekeepingWordfilter;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;

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

        String action = client.post().getString("action");

        if ("addWord".equals(action)) {
            addWord(client, playerDetails);
            return;
        }

        if ("searchTopic".equals(action)) {
            searchTopic(client, tpl);
        }

        if ("deleteWord".equals(action)) {
            deleteWord(client, playerDetails);
            return;
        }

        if ("saveWord".equals(action)) {
            saveWord(client, playerDetails);
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

    private static String getWordfilterPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter";
    }

    public static void addWord (WebConnection client, PlayerDetails playerDetails){
        String addword = client.post().getString("addword");
        boolean isBannable = client.post().getBoolean("isBannable");
        boolean isFilterable = client.post().getBoolean("isFilterable");

        if (addword.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid word");
            client.redirect(getWordfilterPath());
            return;
        }

        var word = HousekeepingWordfilterDao.getWordByWord(addword);

        if (word != null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The word already exists");
            client.redirect(getWordfilterPath());
            return;
        }

        HousekeepingWordfilterDao.createWord(addword, isBannable, isFilterable);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Added the word " + addword + " to Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

        RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {});

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The word '" + addword + "' has been successfully added to wordfilter list");
        client.redirect(getWordfilterPath());
    }

    public static void deleteWord (WebConnection client, PlayerDetails playerDetails) {
        String wordIdStr = client.post().getString("wordId");
        int wordId = !NumberUtils.isParsable(wordIdStr) ? 0 : Integer.parseInt(wordIdStr);

        var word = HousekeepingWordfilterDao.getWordById(wordId);

        if (word == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The word ID not exists");
            client.redirect(getWordfilterPath());
            return;
        }

        HousekeepingWordfilterDao.deleteWord(word.getId());
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted word with the ID " + wordId + " of Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

        RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {});

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The word has been successfully deleted from Wordfilter");
        client.redirect(getWordfilterPath());
    }

    private static void searchTopic(WebConnection client, Template tpl) {
        String query = client.post().getString("searchStr");

        if (StringUtils.isEmpty(query)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid search value");
            client.redirect(getWordfilterPath());
            return;
        }

        List<HousekeepingWordfilter> searchWords = HousekeepingWordfilterDao.searchWords(query);

        tpl.set("query", query);
        tpl.set("searchWordsDetails", searchWords);
        tpl.set("searchEmpty", searchWords.isEmpty());
    }

    public static void saveWord (WebConnection client, PlayerDetails playerDetails) {
        String wordStr = client.post().getString("wordId");
        int wordId = !NumberUtils.isParsable(wordStr) ? 0 : Integer.parseInt(wordStr);
        String saveWord = client.post().getString("saveWord");
        boolean isBannable = client.post().getBoolean("isBannable");
        boolean isFilterable = client.post().getBoolean("isFilterable");

        var word = HousekeepingWordfilterDao.getWordById(wordId);

        if (word == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The word ID not exists");
            client.redirect(getWordfilterPath());
            return;
        }

        if (saveWord.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid word");
            client.redirect(getWordfilterPath());
            return;
        }

        var wordExists = HousekeepingWordfilterDao.getWordByWord(saveWord);

        if (wordExists != null && wordExists.getId() != word.getId()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The word already exists");
            client.redirect(getWordfilterPath());
            return;
        }

        word.setWord(saveWord);
        word.setIsBannable(isBannable);
        word.setIsFilterable(isFilterable);

        HousekeepingWordfilterDao.saveWord(saveWord, isBannable, isFilterable, wordId);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited the word " + saveWord + " of Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

        RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {});

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The word '" + saveWord + "' has been successfully saved");
        client.redirect(getWordfilterPath());
    }
}
