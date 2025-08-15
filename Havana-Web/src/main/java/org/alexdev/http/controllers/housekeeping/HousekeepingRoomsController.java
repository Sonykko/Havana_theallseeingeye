package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRoomDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

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
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if (client.post().queries().size() > 0 && !action.equals("copyRoom")) {
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

            if (!(roomsAdmin.size() > 0)) {
                tpl.set("noResults", true);
            }

            tpl.set("roomsAdmin", roomsAdmin);
        }

        if ("copyRoom".equals(action)) {
            copyRoom(client, playerDetails);
            return;
        }

        tpl.set("pageName", "Room admin");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void copyRoom (WebConnection client, PlayerDetails playerDetails) {
        String roomIdStrg = client.post().getString("roomId");

        if (!StringUtils.isNumeric(roomIdStrg)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please select a valid roomId");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");
            return;
        }

        int roomId = client.post().getInt("roomId");
        var room = RoomDao.getRoomById(roomId);

        if (room == null || room.isPublicRoom()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please select a valid private room");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");
            return;
        }

        RconUtil.sendCommand(RconHeader.COPY_PRIVATE_ROOM, new HashMap<>() {{
            put("moderator", playerDetails.getName());
            put("roomId", roomId);
        }});

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Copy/clone the room with the ID " + room.getId() + " . URL: " + client.request().uri(), client.getIpAddress());

        var clonedRoomId = getClonedRoom(playerDetails);

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Copy/cloned the room with the ID " + room.getId() + ". For edit the room <a href=\"/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/edit?id=" + clonedRoomId + "\">click here</a>");
        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");
    }

    public static int getClonedRoom (PlayerDetails playerDetails) {
        return HousekeepingRoomDao.getClonedRoom(playerDetails.getId());
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
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (!client.get().contains("id") || !(StringUtils.length(client.get().getString("id")) > 0)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "You did not select a room to admin");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");
            return;
        }

        int roomId = -1;
        try {
            roomId = client.get().getInt("id");
            if (roomId < 1000) {
                throw new NumberFormatException("Room ID must be 1000 or greater");
            }
        } catch (NumberFormatException e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Invalid room ID. It must be a number and 1000 or greater.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");
            return;
        }

        Room room = RoomDao.getRoomById(roomId);

        List<Room> RoomAdmin = HousekeepingRoomDao.getRoom1(roomId);

        tpl.set("RoomAdminData", RoomAdmin);

        if (room == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The room does not exist");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/search");
            return;
        } else {
            if (client.post().queries().size() > 0 && !client.session().contains("alertMessages")) {
                try {
                    int category = client.post().getInt("category");
                    String name = client.post().getString("name");
                    String description = client.post().getString("description");
                    int accesstype = client.post().getInt("accesstype");
                    String password = client.post().getString("password");
                    String finalPassword = password.isEmpty() ? "" : password;
                    boolean showOwnerName = client.post().contains("showOwnerName");

                    RconUtil.sendCommand(RconHeader.REFRESH_PRIVATE_ROOM, new HashMap<>() {{
                        put("room", room.getId());
                        put("category", category);
                        put("name", name);
                        put("description", description);
                        put("accesstype", accesstype);
                        put("password", finalPassword);
                        put("showOwnerName", showOwnerName);
                    }});

                    HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited Private Room with the ID " + roomId + ". URL: " + client.request().uri(), client.getIpAddress());

                    client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/rooms/edit?id=" + roomId);

                    client.session().set("alertColour", "success");
                    client.session().set("alertMessage", "The room has been successfully saved");
                    return;
                } catch (NumberFormatException e) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "Invalid format in one of the fields");
                }
            }
        }

        tpl.set("pageName", "Room Admin");
        tpl.set("roomCats", HousekeepingRoomDao.getAllPrivateRoomsCat());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}