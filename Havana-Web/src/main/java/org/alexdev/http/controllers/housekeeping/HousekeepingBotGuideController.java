package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.bot.enums.BotGuideSpeechDefaultType;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingBotGuideDao;
import org.alexdev.http.dao.housekeeping.HousekeepingBotsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingBotGuide;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

public class HousekeepingBotGuideController {
    public static void botguide (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/botguide");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bots")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("createSpeech".equals(action)) {
            createSpeech(client, playerDetails);
            return;
        }

        if ("saveBotGuide".equals(action)) {
            saveBotGuide(client, playerDetails);
            return;
        }

        if ("searchSpeech".equals(action)) {
            searchSpeech(client, tpl);
        }

        if ("saveSpeech".equals(action)) {
            saveSpeech(client, playerDetails);
            return;
        }

        if ("deleteSpeech".equals(action)) {
            deleteSpeech(client, playerDetails);
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "Bot Guide tool");
        tpl.set("botguide", HousekeepingBotsDao.getBotByBotId(GameConfiguration.getInstance().getInteger("botguide.id")));
        tpl.set("botguideSpeech", HousekeepingBotGuideDao.getAllSpeech(currentPage));
        tpl.set("nextSpeeches", HousekeepingBotGuideDao.getAllSpeech(currentPage + 1));
        tpl.set("previousSpeeches", HousekeepingBotGuideDao.getAllSpeech(currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getBotGuidePath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/botguide";
    }

    private static void createSpeech(WebConnection client, PlayerDetails playerDetails) {
        String speechKey = client.post().getString("speechKey").toUpperCase();
        String response = client.post().getString("response");
        String speechTrigger = client.post().getString("speechTrigger").toUpperCase();

        if (speechKey.isEmpty() || response.isEmpty() || speechTrigger.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please fill a all valid values");
            client.redirect(getBotGuidePath());
            return;
        }

        if (speechKey.length() > 255 || response.length() > 255 || speechTrigger.length() > 255) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The values are too long");
            client.redirect(getBotGuidePath());
            return;
        }

        var botGuideSpeech = HousekeepingBotGuideDao.getSpeechBySpeechKey(speechKey);

        if (botGuideSpeech != null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The speech key already exists");
            client.redirect(getBotGuidePath());
            return;
        }

        HousekeepingBotGuideDao.create(speechKey, response, speechTrigger);

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Created Bot Guide speech. URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully created speech");
        client.redirect(getBotGuidePath());
    }

    private static void saveBotGuide(WebConnection client, PlayerDetails playerDetails) {
        String botIdStr = client.post().getString("botId");
        int botId = !NumberUtils.isParsable(botIdStr) ? 0 : Integer.parseInt(botIdStr);
        String name = client.post().getString("name");
        String mission = client.post().getString("mission");
        String figure = client.post().getString("figure");
        String figureFlash = client.post().getString("figureFlash");
        String speech = client.post().getString("speech");

        var bot = HousekeepingBotsDao.getBotByBotId(botId);

        if (bot == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't save the Guide Bot cause not exists");
            client.redirect(getBotGuidePath());
            return;
        }

        if (name.isEmpty() || mission.isEmpty() || figure.isEmpty() || figureFlash.isEmpty() || speech.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter all valid values");
            client.redirect(getBotGuidePath());
            return;
        }

        bot.setName(name);
        bot.setMission(mission);
        bot.setFigure(figure);
        bot.setFigureFlash(figureFlash);
        bot.setSpeeches(speech);

        HousekeepingBotGuideDao.saveGuideBot(bot);

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Updated the Guide Bot details with the ID " + bot.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the Guide Bot details");
        client.redirect(getBotGuidePath());
    }

    private static void searchSpeech(WebConnection client, Template tpl) {
        String query = client.post().getString("searchStr");

        if (StringUtils.isEmpty(query)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid search value");
            client.redirect(getBotGuidePath());
            return;
        }

        List<HousekeepingBotGuide> searchBotGuideSpeech = HousekeepingBotGuideDao.search(query);

        tpl.set("query", query);
        tpl.set("searchBotGuideDetails", searchBotGuideSpeech);
        tpl.set("searchEmpty", searchBotGuideSpeech.isEmpty());
    }

    private static void saveSpeech(WebConnection client, PlayerDetails playerDetails) {
        String speechKeyOriginal = client.post().getString("speechKeyOriginal").toUpperCase();
        String speechKey = client.post().getString("speechKey").toUpperCase();
        String response = client.post().getString("response");
        String speechTrigger = client.post().getString("speechTrigger").toUpperCase();


        var botGuideSpeech = HousekeepingBotGuideDao.getSpeechBySpeechKey(speechKeyOriginal);

        if (botGuideSpeech == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't save the speech cause not exists");
            client.redirect(getBotGuidePath());
            return;
        }

        var botGuideSpeechCheck = HousekeepingBotGuideDao.getSpeechBySpeechKey(speechKey);

        if (!speechKeyOriginal.equalsIgnoreCase(speechKey) && botGuideSpeechCheck != null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Speech key already exists");
            client.redirect(getBotGuidePath());
            return;
        }

        if (response.isEmpty() || speechTrigger.isEmpty() && !speechKey.equalsIgnoreCase(BotGuideSpeechDefaultType.WELCOME.toString()) && !speechKey.equalsIgnoreCase(BotGuideSpeechDefaultType.INTRODUCE.toString())) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter valid name and figures values");
            client.redirect(getBotGuidePath());
            return;
        }

        if (speechKey.equalsIgnoreCase(BotGuideSpeechDefaultType.WELCOME.toString()) || speechKey.equalsIgnoreCase(BotGuideSpeechDefaultType.INTRODUCE.toString())) {
            speechKey = speechKeyOriginal;
            speechTrigger = null;
        }

        botGuideSpeech.setSpeechKey(speechKey);
        botGuideSpeech.setResponse(response);
        botGuideSpeech.setSpeechTrigger(speechTrigger);

        HousekeepingBotGuideDao.save(botGuideSpeech, speechKeyOriginal);

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Updated the Bot Guide speech with the key " + speechKeyOriginal + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the Bot speech");
        client.redirect(getBotGuidePath());
    }

    private static void deleteSpeech(WebConnection client, PlayerDetails playerDetails) {
        String speechKeyOriginal = client.post().getString("speechKeyOriginal");

        var speech = HousekeepingBotGuideDao.getSpeechBySpeechKey(speechKeyOriginal);

        if (speech == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't delete the speech cause not exists");
            client.redirect(getBotGuidePath());
            return;
        }

        if (speechKeyOriginal.equalsIgnoreCase(BotGuideSpeechDefaultType.WELCOME.toString()) || speechKeyOriginal.equalsIgnoreCase(BotGuideSpeechDefaultType.INTRODUCE.toString())) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't delete the default speeches");
            client.redirect(getBotGuidePath());
            return;
        }

        HousekeepingBotGuideDao.delete(speechKeyOriginal);

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Deleted the Bot Guide speech with the key " + speechKeyOriginal + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully deleted the speech");
        client.redirect(getBotGuidePath());
    }
}