package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
//import org.alexdev.havana.dao.mysql.CFHDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRoomDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH);
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

            tpl.set("housekeepingManager", HousekeepingManager.getInstance());

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

    public static void cfh_logs_search(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/cfh_logs");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

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

           // int currentPage = 0;

           // if (client.get().contains("pageSearch")) {
                // currentPage = Integer.parseInt(client.get().getString("pageSearch"));
            // }

            List<String> whitelistColumns = new ArrayList<>();
            whitelistColumns.add("user");
            whitelistColumns.add("message");
            whitelistColumns.add("room");
            whitelistColumns.add("room_id");
            whitelistColumns.add("status");
            whitelistColumns.add("moderator");

            List<Map<String, Object>> CFHListSearch = null;

            if (whitelistColumns.contains(field)) {
                CFHListSearch = HousekeepingCFHDao.searchCFHlog(type, field, input);
            } //else {
                //CFHListSearch = new ArrayList<>();
           // }

            // List<Map<String, Object>> CFHList = null;
            //List<Map<String, Object>> CFHList = new ArrayList<>();
            //CFHList.add(CFHLogs);

            //CFHList = HousekeepingCFHDao.getCFHlog();
            //CFHList = new ArrayList<>();

            tpl.set("searchChatlogs", CFHListSearch);

        /*} else {

            int currentPage = 0;

            if (client.get().contains("pageSearch")) {
                currentPage = Integer.parseInt(client.get().getString("pageSearch"));
            }

            String sortBy = "cfh_id"; // Cambiar a la columna correspondiente

            if (client.get().contains("sortSearch")) {
                if (client.get().getString("sort").equals("room_id") ||
                        client.get().getString("sort").equals("cfh_id")) { // Cambiar a la columna correspondiente
                    sortBy = client.get().getString("sort");
                }
            }*/

            // Template tpl = client.template("housekeeping/dashboard");
            tpl.set("housekeepingManager", HousekeepingManager.getInstance());

            //tpl.set("pageName", "CFH Logs");
            //tpl.set("cfhlogsSearch", HousekeepingCFHDao.searchCFHlog(currentPage, sortBy));
            //tpl.set("nextCFHlogsSearch", HousekeepingCFHDao.getCFHlog(currentPage + 1, sortBy));
            //tpl.set("previousCFHlogsSearch", HousekeepingCFHDao.getCFHlog(currentPage - 1, sortBy));
            //tpl.set("pageSearch", currentPage);
            //tpl.set("sortSearch", sortBy);
            tpl.render();

            // Delete alert after it's been rendered
            client.session().delete("alertMessage");
        }
    }

    public static void roomsearch(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/room_chatlogs");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        if (client.post().queries().size() > 0) {
            String field = client.post().getString("searchField");
            String input = client.post().getString("searchQuery");
            String type = client.post().getString("searchType");

            List<String> whitelistColumns = new ArrayList<>();
            whitelistColumns.add("id");
            whitelistColumns.add("message");
            whitelistColumns.add("room_id");
            whitelistColumns.add("room_name");

            List<Map<String, Object>> chatLogs = null;

            if (whitelistColumns.contains(field)) {
                chatLogs = HousekeepingRoomDao.searchChatLogs(type, field, input);
            } else {
                chatLogs = new ArrayList<>();
            }

            tpl.set("searchChatlogs", chatLogs);
        }

        tpl.set("pageName", "Room Chatlogs");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void userchatlogs(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/user_chatlogs");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        List<Map<String, Object>> chatLogs = new ArrayList<>();
        List<Map<String, Object>> messengerMessages = new ArrayList<>();

        if (client.post().queries().size() > 0 || client.get().queries().size() > 0) {
            String userIdParam = client.get().getString("chatId");

            if (!userIdParam.isEmpty()) {
                try {
                    int userId = Integer.parseInt(userIdParam);

                    chatLogs = HousekeepingRoomDao.getChatlogsByUserId(userId);
                    messengerMessages = HousekeepingRoomDao.getMessengerMessagesByUserId(userId);

                    for (Map<String, Object> chatlog : chatLogs) {
                        chatlog.put("logType", "Chatlog");
                    }

                    for (Map<String, Object> message : messengerMessages) {
                        message.put("logType", "MessengerMessage");
                    }
                } catch (NumberFormatException e) {
                    // Manejar la excepción si chatId no es un número válido
                }
            }
        }

        List<Map<String, Object>> userChatlogs = new ArrayList<>();
        userChatlogs.addAll(chatLogs);
        userChatlogs.addAll(messengerMessages);
        Collections.sort(userChatlogs, (a, b) -> Long.compare((long) b.get("timestamp"), (long) a.get("timestamp")));

        tpl.set("userChatlogs", userChatlogs);
        tpl.set("pageName", "User Chatlogs");
        tpl.render();

        // Elimina el mensaje de alerta después de renderizarlo
        client.session().delete("alertMessage");
    }
}