package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.BanDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class HousekeepingBansController {
    public static void bans(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/users_bans");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH);
            return;
        }

        tpl.set("bans", BanDao.getActiveBans());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH);
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

                // Delete alert after it's been rendered
                client.session().delete("alertMessage");
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

            tpl.set("players", players);
        }

        tpl.set("bans", BanDao.getActiveBans());

        int currentPage = 0;

        if (client.get().contains("pageKick")) {
            currentPage = Integer.parseInt(client.get().getString("pageKick"));
        }

        String kickSortBy = "id";

        tpl.set("remoteKickLogs", HousekeepingCommandsDao.RemoteKickLogs(currentPage, kickSortBy));
        tpl.set("nextremoteKickLogs", HousekeepingCommandsDao.RemoteKickLogs(currentPage + 1, kickSortBy));
        tpl.set("previousremoteKickLogs", HousekeepingCommandsDao.RemoteKickLogs(currentPage - 1, kickSortBy));
        tpl.set("page", currentPage);
        tpl.set("kickSortBy", kickSortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}