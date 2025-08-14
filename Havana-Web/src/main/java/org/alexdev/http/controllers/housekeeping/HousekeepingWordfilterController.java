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
            addWord(client, playerDetails);
            return;
        }

        if (client.get().contains("delete")) {
            deleteWord(client, playerDetails);
            return;
        }

        if (client.get().contains("edit")) {
            var word = HousekeepingWordfilterDao.getWordById(client.get().getInt("edit"));
            tpl.set("wordEdit", word);
            tpl.set("isWordEdit", true);
        } else {
            tpl.set("isWordEdit", false);
        }

        if (client.post().contains("saveWord")) {
            saveWord(client, playerDetails);
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

    private static String getWordfilterPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/wordfilter";
    }

    public static void addWord (WebConnection client, PlayerDetails playerDetails){
        String addword = client.post().getString("addword");
        String isBannable = client.post().getString("isBannable");
        String isFilterable = client.post().getString("isFilterable");

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

        if (!isBannable.equals("0") && !isBannable.equals("1") || !isFilterable.equals("0") && !isFilterable.equals("1")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Enter a valid bannable or filtrable value");
            client.redirect(getWordfilterPath());
            return;
        }

        boolean isBannableBool = client.post().getBoolean("isBannable");
        boolean isFilterableBool = client.post().getBoolean("isFilterable");

        HousekeepingWordfilterDao.createWord(addword, isBannableBool, isFilterableBool);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Added the word " + addword + " to Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

        RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {{
        }});

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The word has been successfully added to wordfilter list");
        client.redirect(getWordfilterPath());
    }

    public static void deleteWord (WebConnection client, PlayerDetails playerDetails) {
        String wordId = client.get().getString("delete");

        if (!StringUtils.isNumeric(wordId)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid word ID");
            client.redirect(getWordfilterPath());
            return;
        }

        int wordIdInt = Integer.parseInt(wordId);
        var word = HousekeepingWordfilterDao.getWordById(wordIdInt);

        if (word == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The word ID not exists");
            client.redirect(getWordfilterPath());
            return;
        }

        HousekeepingWordfilterDao.deleteWord(word.getId());
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted word with the ID " + wordId + " of Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

        RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {{
        }});

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The word has been successfully deleted from Wordfilter");
        client.redirect(getWordfilterPath());
    }

    public static void saveWord (WebConnection client, PlayerDetails playerDetails) {
        String saveWord = client.post().getString("saveWord");
        String isBannable = client.post().getString("isBannable");
        String isFilterable = client.post().getString("isFilterable");

        if (!StringUtils.isNumeric(client.get().getString("edit"))) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid word ID");
            client.redirect(getWordfilterPath());
            return;
        }

        int wordId = client.get().getInt("edit");

        if (client.post().getString("saveWord").isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid word");
            client.redirect(getWordfilterPath() + "?edit=" + wordId);
            return;
        }

        if (!isBannable.equals("0") && !isBannable.equals("1") || !isFilterable.equals("0") && !isFilterable.equals("1")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Enter a valid bannable or filtrable value");
            client.redirect(getWordfilterPath());
            return;
        }

        boolean isBannableBool = client.post().getBoolean("isBannable");
        boolean isFilterableBool = client.post().getBoolean("isFilterable");

        var wordEdit = HousekeepingWordfilterDao.getWordById(wordId);
        String currentWord = wordEdit.getWord();

        if (!saveWord.equalsIgnoreCase(currentWord)) {
            var wordExists = HousekeepingWordfilterDao.getWordByWord(saveWord);

            if (wordExists != null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The word already exists");
                client.redirect(getWordfilterPath() + "?edit=" + wordId);
                return;
            }
        }

        HousekeepingWordfilterDao.saveWord(saveWord, isBannableBool, isFilterableBool, wordId);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited the word " + saveWord + " of Wordfilter. URL: " + client.request().uri(), client.getIpAddress());

        RconUtil.sendCommand(RconHeader.REFRESH_WORDFILTER, new HashMap<>() {{
        }});

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The word has been successfully saved");
        client.redirect(getWordfilterPath());
    }
}
