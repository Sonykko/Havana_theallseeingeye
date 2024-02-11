package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRoomDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.HashMap;
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

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            return;
        }

        if (client.post().queries().size() > 0) {
            String[] fieldCheck = new String[]{"searchField", "searchQuery", "searchType"};

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
                if (client.post().getString("searchField").equals("ownerName")) {
                    roomsAdmin = HousekeepingRoomDao.searchRoom1("ownerName", field, input);
                } else {
                    roomsAdmin = HousekeepingRoomDao.searchRoom1(type, field, input);
                }
            } else {
                roomsAdmin = new ArrayList<>();
            }

            tpl.set("roomsAdmin", roomsAdmin);
        }

        tpl.set("pageName", "Room admin");
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
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
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

        List<Room> RoomAdmin = HousekeepingRoomDao.getRoom1(client.get().getInt("id"));

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
                String password = "";
                if (accesstype == 2) {
                    password = client.post().getString("password");
                }

                HousekeepingRoomDao.updateRoom(roomId, category, name, description, accesstype, password);

                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The room has been successfully saved");

                RconUtil.sendCommand(RconHeader.REFRESH_NAVIGATOR, new HashMap<>());

                return;
            }
        }

        tpl.set("pageName", "Room Admin");
        tpl.set("roomCats", HousekeepingRoomDao.getAllPrivateRoomsCat());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}