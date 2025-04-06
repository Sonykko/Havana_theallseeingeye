package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPromotionDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class HousekeepingStaffPicksController {
    public static void staff_picks(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/staff_picks");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "articles/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        long pickId = 0;
        String type = "";

        if (client.post().contains("create") && client.post().contains("type")) {
            createStaffPick(client, playerDetails);
            return;
        }

        if (client.get().contains("delete")) {
            deleteStaffPick(client, playerDetails);
            return;
        }

        if (client.get().contains("edit")) {
            editStaffPick(client, tpl);
        } else {
            tpl.set("editingPick", false);
            client.session().set("editSession", pickId);
            client.session().set("typeSession", type);
        }

        if (client.post().getString("sid").equals("7")) {
            saveStaffPick(client, playerDetails);
            return;
        }

        List<Map<String, Object>> staffPicksListRoomGroup = HousekeepingPromotionDao.getAllPickReco(1);

        for (Map<String, Object> staffPick : staffPicksListRoomGroup) {
            int pickIdRoomGroup = (int) staffPick.get("ID");
            String typeRoomGroup = (String) staffPick.get("type");

            if ("ROOM".equals(typeRoomGroup)) {
                Room room = RoomDao.getRoomById(pickIdRoomGroup);

                if (room != null) {
                    staffPick.put("roomName", room.getData().getName());
                    if (room.isPublicRoom()) {
                        staffPick.put("roomDescription", RoomDao.getDescriptionPublicRoom(room.getData().getId()));
                    } else {
                        staffPick.put("roomDescription", room.getData().getDescription());
                    }
                    staffPick.put("roomOwner", room.getData().getOwnerName());
                    staffPick.put("roomId", room.getData().getId());
                    staffPick.put("pickId", pickIdRoomGroup);
                    staffPick.put("type", typeRoomGroup);
                }
            }

            if ("GROUP".equals(typeRoomGroup)) {
                Group group = GroupDao.getGroup(pickIdRoomGroup);

                if (group != null) {
                    staffPick.put("groupName", group.getName());
                    staffPick.put("groupDescription", group.getDescription());
                    staffPick.put("groupOwner", PlayerDao.getDetails(group.getOwnerId()).getName());
                    staffPick.put("groupId", group.getId());
                    staffPick.put("pickId", pickIdRoomGroup);
                    staffPick.put("type", typeRoomGroup);
                }
            }
        }

        tpl.set("pageName", "Staff Picks");
        tpl.set("StaffPicksList", staffPicksListRoomGroup);
        tpl.set("pickId", pickId);
        tpl.set("type", type);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getStaffPicksPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/staff_picks";
    }

    public static void createStaffPick (WebConnection client, PlayerDetails playerDetails) {
        String create = client.post().getString("create");

        if (StringUtils.isEmpty(create) || !StringUtils.isNumeric(create)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
            client.redirect(getStaffPicksPath());
            return;
        }

        long pickId = 0;
        String type = "";

        pickId = client.post().getInt("create");
        type = client.post().getString("type");

        if (!type.equals("GROUP") && !type.equals("ROOM")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid type");
            client.redirect(getStaffPicksPath());
            return;
        }

        Group group = GroupDao.getGroup((int) pickId);
        Room room = RoomDao.getRoomById((int) pickId);

        if (group == null && room == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID does not exist");
            client.redirect(getStaffPicksPath());
            return;
        }

        List<Map<String, Object>> checkPickReco = HousekeepingPromotionDao.EditPickReco((int) pickId, type, 1);
        if (!checkPickReco.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID already exists");
            client.redirect(getStaffPicksPath());
            return;
        }

        HousekeepingPromotionDao.createPickReco((int) pickId, type, 1);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created Staff Pick with the ID " + pickId + " of type " + type + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Staff Pick has been successfully created");
        client.redirect(getStaffPicksPath());
    }

    public static void deleteStaffPick (WebConnection client, PlayerDetails playerDetails) {
        String delete = client.get().getString("delete");

        if (StringUtils.isEmpty(delete) || !StringUtils.isNumeric(delete)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
            client.redirect(getStaffPicksPath());
            return;
        }

        long pickId = 0;
        String type = "";

        pickId = client.get().getInt("delete");
        type = client.get().getString("type");

        if (!type.equals("GROUP") && !type.equals("ROOM")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid type");
            client.redirect(getStaffPicksPath());
            return;
        }

        Group group = GroupDao.getGroup((int) pickId);
        Room room = RoomDao.getRoomById((int) pickId);

        if (group == null && room == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID does not exist");
            client.redirect(getStaffPicksPath());
            return;
        }

        List<Map<String, Object>> checkPickReco = HousekeepingPromotionDao.EditPickReco((int) pickId, type, 1);
        if (checkPickReco.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID does not exist in records");
            client.redirect(getStaffPicksPath());
            return;
        }

        HousekeepingPromotionDao.deletePickReco((int) pickId, type, 1);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted Staff Pick with the ID " + pickId + " of type " + type + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Staff Pick has been successfully deleted");
        client.redirect(getStaffPicksPath());
    }

    public static void editStaffPick (WebConnection client, Template tpl) {
        String edit = client.get().getString("edit");

        if (StringUtils.isEmpty(edit) || !StringUtils.isNumeric(edit)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
            client.redirect(getStaffPicksPath());
            return;
        }

        long pickId = 0;
        String type = "";

        pickId = client.get().getInt("edit");
        type = client.get().getString("type");

        Group groupEdit = GroupDao.getGroup((int) pickId);
        Room roomEdit = RoomDao.getRoomById((int) pickId);
        List<Map<String, Object>> checkStaffPick = HousekeepingPromotionDao.EditPickReco((int) pickId, type, 1);

        if (groupEdit == null && roomEdit == null || checkStaffPick.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID not exists");
            client.redirect(getStaffPicksPath());
            return;
        }

        if (!type.equals("GROUP") && !type.equals("ROOM")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid type");
            client.redirect(getStaffPicksPath());
            return;
        }

        List<Map<String, Object>> staffPicksListRoomGroupEdit = HousekeepingPromotionDao.EditPickReco((int) pickId, type, 1);

        for (Map<String, Object> staffPickEdit : staffPicksListRoomGroupEdit) {
            int pickIdRoomGroup = (int) staffPickEdit.get("ID");
            String typeRoomGroup = (String) staffPickEdit.get("type");

            if ("ROOM".equals(typeRoomGroup)) {
                Room room = RoomDao.getRoomById(pickIdRoomGroup);

                staffPickEdit.put("name", room.getData().getName());
                if (room.getData().getOwnerId() == 0) {
                    staffPickEdit.put("description", RoomDao.getDescriptionPublicRoom(room.getData().getId()));
                } else {
                    staffPickEdit.put("description", room.getData().getDescription());
                }
                staffPickEdit.put("owner", room.getData().getOwnerName());
                staffPickEdit.put("id", room.getData().getId());
                staffPickEdit.put("pickId", pickIdRoomGroup);
                staffPickEdit.put("type", typeRoomGroup);
            }

            if ("GROUP".equals(typeRoomGroup)) {
                Group group = GroupDao.getGroup(pickIdRoomGroup);

                staffPickEdit.put("name", group.getName());
                staffPickEdit.put("description", group.getDescription());
                staffPickEdit.put("owner", PlayerDao.getDetails(group.getOwnerId()).getName());
                staffPickEdit.put("id", group.getId());
                staffPickEdit.put("pickId", pickIdRoomGroup);
                staffPickEdit.put("type", typeRoomGroup);
            }


            tpl.set("editingPick", true);
            tpl.set("EditStaffPick", staffPicksListRoomGroupEdit);
            client.session().set("editSession", pickId);
            client.session().set("typeSession", type);
        }
    }

    public static void saveStaffPick (WebConnection client, PlayerDetails playerDetails) {
        String save = client.post().getString("IdSave");

        if (StringUtils.isEmpty(save) || !StringUtils.isNumeric(save)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
            client.redirect(getStaffPicksPath());

            return;
        }

        long pickId = 0;
        String type = "";

        pickId = client.session().getInt("editSession");
        type = client.session().getString("typeSession");

        String pickIdSave = client.post().getString("IdSave");
        String typeSave = client.post().getString("typeSave");
        int isPickedSave = client.post().getInt("isPickedSave");

        if (!type.equals("GROUP") && !type.equals("ROOM") || !typeSave.equals("GROUP") && !typeSave.equals("ROOM")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid type");
            client.redirect(getStaffPicksPath() + "?edit=" + pickId + "&type=" + type);
            return;
        }

        Group group = GroupDao.getGroup((int) pickId);
        Room room = RoomDao.getRoomById((int) pickId);
        Group groupSave = GroupDao.getGroup((int) pickId);
        Room roomSave = RoomDao.getRoomById((int) pickId);

        if (group == null && room == null || groupSave == null && roomSave == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID not exists in records");
            client.redirect(getStaffPicksPath());
            return;
        }

        if (isPickedSave == 0 && !typeSave.equals("GROUP")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't set as Recommended group a room");
            client.redirect(getStaffPicksPath() + "?edit=" + pickId + "&type=" + type);
            return;
        }

        List<Map<String, Object>> checkPickReco = HousekeepingPromotionDao.EditPickReco((int) pickId, type, 1);
        if (checkPickReco.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Pick ID does not exist in records");
            client.redirect(getStaffPicksPath());
            return;
        }

        HousekeepingPromotionDao.SavePickReco(pickIdSave, typeSave, isPickedSave, (int) pickId, type, 1);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited Staff Pick with the ID " + pickId + " (before), " + pickIdSave + "(after). URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Staff Pick has been successfully saved");
        client.redirect(getStaffPicksPath());
    }
}
