package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingBotsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingBot;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;

public class HousekeepingBotsController {
    public static void bots (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/bots");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bots")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("searchBot".equals(action)) {
            searchBot(client, tpl);
        }

        if ("saveBot".equals(action)) {
            saveBot(client, playerDetails);
            return;
        }

        tpl.set("pageName", "Bot admin");
        tpl.set("bots", HousekeepingBotsDao.getAllBots());
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getBotsPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/bots";
    }

    private static void searchBot(WebConnection client, Template tpl) {
        String query = client.post().getString("searchStr");

        if (StringUtils.isEmpty(query)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid search value");
            client.redirect(getBotsPath());
            return;
        }

        List<HousekeepingBot> searchBots = HousekeepingBotsDao.search(query);

        tpl.set("query", query);
        tpl.set("searchBotsDetails", searchBots);
        tpl.set("searchEmpty", searchBots.isEmpty());
    }

    private static void saveBot(WebConnection client, PlayerDetails playerDetails) {
        String botIdStr = client.post().getString("botId");
        int botId = !NumberUtils.isParsable(botIdStr) ? 0 : Integer.parseInt(botIdStr);
        String name = client.post().getString("name");
        String mission = client.post().getString("mission");
        String figure = client.post().getString("figure");
        String figureFlash = client.post().getString("figureFlash");
        String speech = client.post().getString("speech");
        String response = client.post().getString("response");
        String unrecognisedSpeech = client.post().getString("unrecognisedSpeech");
        String drink = client.post().getString("drink");

        var bot = HousekeepingBotsDao.getBotByBotId(botId);

        if (bot == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't save the bot cause not exists");
            client.redirect(getBotsPath());
            return;
        }

        if (name.isEmpty() || figure.isEmpty() || figureFlash.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter valid name and figures values");
            client.redirect(getBotsPath());
            return;
        }

        bot.setName(name);
        bot.setMission(mission);
        bot.setFigure(figure);
        bot.setFigureFlash(figureFlash);
        bot.setSpeeches(speech);
        bot.setResponses(response);
        bot.setUnrecognisedSpeech(unrecognisedSpeech);
        bot.setDrinks(drink);

        HousekeepingBotsDao.save(bot);

        RconUtil.sendCommand(RconHeader.REFRESH_BOTS, new HashMap<>() {{
            put("roomId", bot.getRoomId());

        }});

        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Updated the Bot with the ID " + bot.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the Bot with the ID " + bot.getId());
        client.redirect(getBotsPath());
    }
}