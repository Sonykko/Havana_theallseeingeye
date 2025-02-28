package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.BanDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class HousekeepingBansController {
    public static void bans(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/users_banskicks");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().queries().size() > 0) {
            String[] fieldCheck = new String[]{"searchField", "searchQuery", "searchType" };

            for (String field : fieldCheck) {
                if (client.post().contains(field) && client.post().getString(field).length() > 0) {
                    continue;
                }

                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "You need to enter all fields");
                tpl.render();
                return;
            }

            String field = client.post().getString("searchField");
            String input = client.post().getString("searchQuery");
            String type = client.post().getString("searchType");

            List<String> whitelistColumns = new ArrayList<>();
            whitelistColumns.add("username");
            whitelistColumns.add("id");


            List<PlayerDetails> players = null;

            if (whitelistColumns.contains(field)) {
                players = HousekeepingPlayerDao.search(type, field, input);
            } else {
                players = new ArrayList<>();
            }

            if (!(players.size() > 0)) {
                tpl.set("noResults", true);
            }

            tpl.set("players", players);
        }

        int currentPageBan = 0;

        if (client.get().contains("pageBan")) {
            currentPageBan = Integer.parseInt(client.get().getString("pageBan"));
        }

        String sortByBan = "banned_at";

        if (client.get().contains("sortBan")) {
            if (client.get().getString("sortBan").equals("banned_at") ||
                    client.get().getString("sortBan").equals("banned_until")) {
                sortByBan = client.get().getString("sortBan");
            }
        }

        tpl.set("pageName", "Remote Ban & Kick");
        tpl.set("bans", BanDao.getActiveBans(currentPageBan, sortByBan));
        tpl.set("nextBans", BanDao.getActiveBans(currentPageBan + 1, sortByBan));
        tpl.set("previousBans", BanDao.getActiveBans(currentPageBan - 1, sortByBan));
        tpl.set("pageBan", currentPageBan);
        tpl.set("sortByBan", sortByBan);
        tpl.render();

        int currentPageKick = 0;

        if (client.get().contains("pageKick")) {
            currentPageKick = Integer.parseInt(client.get().getString("pageKick"));
        }

        String kickSortBy = "id";

        tpl.set("remoteKickLogs", HousekeepingCommandsDao.RemoteKickLogs(currentPageKick, kickSortBy));
        tpl.set("nextremoteKickLogs", HousekeepingCommandsDao.RemoteKickLogs(currentPageKick + 1, kickSortBy));
        tpl.set("previousremoteKickLogs", HousekeepingCommandsDao.RemoteKickLogs(currentPageKick - 1, kickSortBy));
        tpl.set("pageKick", currentPageKick);
        tpl.set("kickSortBy", kickSortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}