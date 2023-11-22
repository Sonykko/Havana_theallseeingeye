package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.DateUtil;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRoomDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HousekeepingRoomsController {
    public static void search(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/rooms_search");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");  // Cambiado de playerDetails a roomDetails

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {  // Cambiado de user/search a room/search
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
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
            whitelistColumns.add("id");
            whitelistColumns.add("name");
            whitelistColumns.add("description");
            whitelistColumns.add("owner_id");
            whitelistColumns.add("ownerName");

            List<Map<String, Object>> roomsAdmin = null;

            if (whitelistColumns.contains(field)) {
                roomsAdmin = HousekeepingRoomDao.searchRoom(type, field, input);  // Cambiado de HousekeepingPlayerDao a HousekeepingRoomDao
            } else {
                roomsAdmin = new ArrayList<>();
            }

            tpl.set("roomsAdmin", roomsAdmin);
        }

        tpl.set("pageName", "Room admin");  // Cambiado de Search Users a Search Rooms
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void editRoom(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/rooms_edit");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/edit")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        if (!client.get().contains("id")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "You did not select a room to admin");
        }

        if (client.post().queries().size() > 0) {
            String[] fieldCheck = new String[]{"category", "name", "description", "showname", "accesstype", "pixels"};

            for (String field : fieldCheck) {
                if (client.post().contains(field) && client.post().getString(field).length() > 0) {
                    continue;
                }

                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "You need to enter all fields. The " + field + " field is missing.");
            }
        }

        List<Map<String, Object>> RoomAdmin = HousekeepingRoomDao.getRoom(client.get().getString("id"));

        tpl.set("RoomAdminData", RoomAdmin);

        if (RoomAdmin == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The room does not exist");
        } else {
            if (client.post().queries().size() > 0 && !client.session().contains("alertMessages")) {
                int roomId = client.post().getInt("roomId");
                int category = client.post().getInt("category");
                String name = client.post().getString("name");
                String description = client.post().getString("description");
                int accesstype = client.post().getInt("accesstype");

                HousekeepingRoomDao.updateRoom(roomId, category, name, description, accesstype);

                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/edit?id=" + client.session().getInt("roomId"));

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The room has been successfully saved");

                return;
            }
        }

        tpl.set("pageName", "Room Admin");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void users(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/users");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/edit")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
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

        tpl.set("pageName", "Search Users");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

}
