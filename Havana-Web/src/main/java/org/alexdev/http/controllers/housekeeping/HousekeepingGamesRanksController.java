package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHTopicsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingGamesRanksDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingGamesRanks;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.GameType;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;

public class HousekeepingGamesRanksController {
    public static void games_ranks (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/games_ranks");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/ranks")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("createRank".equals(action)) {
            createRank(client, playerDetails);
            return;
        }

        if ("searchRank".equals(action)) {
            searchRank(client, tpl);
        }

        if ("saveRank".equals(action)) {
            saveRank(client, playerDetails);
            return;
        }

        if ("deleteRank".equals(action)) {
            deleteRank(client, playerDetails);
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "Games ranks tool");
        tpl.set("gameTypes", GameType.values());
        tpl.set("ranks", HousekeepingGamesRanksDao.getGamesRanksByPage(currentPage));
        tpl.set("nextRanks", HousekeepingCFHTopicsDao.getCFHTopicsByPage(currentPage + 1));
        tpl.set("previousRanks", HousekeepingCFHTopicsDao.getCFHTopicsByPage(currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getGamesRanksPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/games_ranks";
    }

    private static void createRank(WebConnection client, PlayerDetails playerDetails) {
        String gameTypeStr = client.post().getString("gameType");
        String title = client.post().getString("rankTitle");
        String minPointsStr = client.post().getString("rankMinPoints");
        String maxPointsStr = client.post().getString("rankMaxPoints");
        int minPoints = !NumberUtils.isParsable(minPointsStr) ? 0 : Integer.parseInt(minPointsStr);
        int maxPoints = !NumberUtils.isParsable(maxPointsStr) ? 0 : Integer.parseInt(maxPointsStr);

        if (title.isEmpty() || gameTypeStr.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter all valid values");
            client.redirect(getGamesRanksPath());
            return;
        }

        GameType gameType = Arrays.stream(GameType.values())
                .filter(gt -> gt.name().equalsIgnoreCase(gameTypeStr))
                .findFirst()
                .orElse(null);

        if (gameType == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid game type");
            client.redirect(getGamesRanksPath());
            return;
        }

        HousekeepingGamesRanksDao.create(title, gameType, minPoints, maxPoints);

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created game rank. URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully created game rank");
        client.redirect(getGamesRanksPath());
    }

    private static void searchRank(WebConnection client, Template tpl) {
        String query = client.post().getString("searchStr");

        if (StringUtils.isEmpty(query)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid search value");
            client.redirect(getGamesRanksPath());
            return;
        }

        List<HousekeepingGamesRanks> searchGamesRanks = HousekeepingGamesRanksDao.searchGamesRanks(query);

        tpl.set("query", query);
        tpl.set("searchGamesRanksDetails", searchGamesRanks);
        tpl.set("searchEmpty", searchGamesRanks.isEmpty());
    }

    private static void saveRank(WebConnection client, PlayerDetails playerDetails) {
        String rankIdStr = client.post().getString("rankId");
        int rankId = !NumberUtils.isParsable(rankIdStr) ? 0 : Integer.parseInt(rankIdStr);
        String gameTypeStr = client.post().getString("gameType");
        String title = client.post().getString("rankTitle");
        String minPointsStr = client.post().getString("rankMinPoints");
        String maxPointsStr = client.post().getString("rankMaxPoints");
        int minPoints = !NumberUtils.isParsable(minPointsStr) ? 0 : Integer.parseInt(minPointsStr);
        int maxPoints = !NumberUtils.isParsable(maxPointsStr) ? 0 : Integer.parseInt(maxPointsStr);

        if (title.isEmpty() || gameTypeStr.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter all valid values");
            client.redirect(getGamesRanksPath());
            return;
        }

        GameType gameType = Arrays.stream(GameType.values())
                .filter(gt -> gt.name().equalsIgnoreCase(gameTypeStr))
                .findFirst()
                .orElse(null);

        if (gameType == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid game type");
            client.redirect(getGamesRanksPath());
            return;
        }

        var gameRank = HousekeepingGamesRanksDao.getGameRankById(rankId);

        if (gameRank == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't save the rank cause not exists");
            client.redirect(getGamesRanksPath());
            return;
        }

        gameRank.setType(gameType);
        gameRank.setTitle(title);
        gameRank.setMinPoints(minPoints);
        gameRank.setMaxPoints(maxPoints);

        HousekeepingGamesRanksDao.save(gameRank);

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Updated the game rank with the ID " + gameRank.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the game rank with the ID " + gameRank.getId());
        client.redirect(getGamesRanksPath());
    }

    private static void deleteRank(WebConnection client, PlayerDetails playerDetails) {
        String rankIdStr = client.post().getString("rankId");
        int rankId = !NumberUtils.isParsable(rankIdStr) ? 0 : Integer.parseInt(rankIdStr);

        var gameRank = HousekeepingGamesRanksDao.getGameRankById(rankId);

        if (gameRank == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't delete the rank cause not exists");
            client.redirect(getGamesRanksPath());
            return;
        }

        HousekeepingGamesRanksDao.delete(gameRank.getId());

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted the game rank with the ID " + gameRank.getId() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully deleted the game rank");
        client.redirect(getGamesRanksPath());
    }
}