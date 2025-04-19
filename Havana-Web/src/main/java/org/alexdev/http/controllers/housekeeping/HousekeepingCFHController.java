package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRoomDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

import java.util.*;

public class HousekeepingCFHController {

    /**
     * Handle the /housekeeping URI request
     *
     * @param client the connection
     */
    public static void cfh_logs(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/cfh_logs");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/edit")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().queries().size() > 0) {
            String[] fieldCheck = new String[]{"searchField1", "searchQuery1", "searchType1" };

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

            List<Map<String, Object>> CFHList = new ArrayList<>();

            tpl.set("searchChatlogs", CFHList);

        } else {

            int currentPage = 0;

            if (client.get().contains("page")) {
                currentPage = Integer.parseInt(client.get().getString("page"));
            }

            String sortBy = "cfh_id";

            if (client.get().contains("sort")) {
                if (client.get().getString("sort").equals("room_id") ||
                        client.get().getString("sort").equals("cfh_id")) {
                    sortBy = client.get().getString("sort");
                }
            }

            tpl.set("pageName", "CFH Logs");
            tpl.set("cfhlogs", HousekeepingCFHDao.getCFHlog(currentPage, sortBy));
            tpl.set("nextCFHlogs", HousekeepingCFHDao.getCFHlog(currentPage + 1, sortBy));
            tpl.set("previousCFHlogs", HousekeepingCFHDao.getCFHlog(currentPage - 1, sortBy));
            tpl.set("page", currentPage);
            tpl.set("sortBy", sortBy);
            tpl.render();

            // Delete alert after it's been rendered
            client.session().delete("alertMessage");
        }
    }

    public static void cfh_actions(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/cfh_actions");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().contains("messageReply")) {
            String cryIdReply = client.get().getString("cryId");
            String messageReply = client.post().getString("messageReply");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/cfh.reply?cryIdReply=" + cryIdReply + "&messageReply=" + messageReply);
            return;
        }

        if (client.post().contains("block")) {
            String cryIdBlock = client.get().getString("cryId");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/cfh.block?cryIdBlock=" + cryIdBlock);
            return;
        }

        if (client.post().contains("follow")) {
            String cryIdFollow= client.get().getString("cryId");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/cfh.follow?cryIdFollow=" + cryIdFollow + "&moderatorFollow=" + playerDetails.getName());
            return;
        }

        tpl.set("pageName", "CFH Action");
        tpl.set("cfhlogsAction", HousekeepingCFHDao.getCFHlogAction(client.get().getString("cryId")));
        tpl.set("defaultReply", GameConfiguration.getInstance().getString("rcon.cfh.reply.message"));
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void cfh_mods(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/cfh_mods");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().queries().size() > 0) {
            String[] fieldCheck = new String[]{"searchField1", "searchQuery1", "searchType1" };

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

            List<Map<String, Object>> CFHList = new ArrayList<>();

            tpl.set("searchChatlogs", CFHList);

        } else {

            int currentPage = 0;

            if (client.get().contains("page")) {
                currentPage = Integer.parseInt(client.get().getString("page"));
            }

            String sortBy = "cfh_id";

            if (client.get().contains("sort")) {
                if (client.get().getString("sort").equals("room_id") ||
                        client.get().getString("sort").equals("cfh_id")) {
                    sortBy = client.get().getString("sort");
                }
            }

            tpl.set("pageName", "Remote CFH admin");
            tpl.set("cfhlogs", HousekeepingCFHDao.getCFHlog(currentPage, sortBy));
            tpl.set("nextCFHlogs", HousekeepingCFHDao.getCFHlog(currentPage + 1, sortBy));
            tpl.set("previousCFHlogs", HousekeepingCFHDao.getCFHlog(currentPage - 1, sortBy));
            tpl.set("page", currentPage);
            tpl.set("sortBy", sortBy);
            tpl.render();

            // Delete alert after it's been rendered
            client.session().delete("alertMessage");
        }
    }
}