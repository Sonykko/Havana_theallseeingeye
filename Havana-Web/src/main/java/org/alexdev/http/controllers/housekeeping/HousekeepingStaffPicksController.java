package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
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
            return;
        }

        int pickId = 0;
        String type = "";

        if (client.post().contains("create") && client.post().contains("type")) {
            pickId = client.post().getInt("create");
            type = client.post().getString("type");

            if (StringUtils.isNumeric(client.post().getString("create")) && client.post().getInt("create") > 0) {
                HousekeepingPromotionDao.createPickReco(pickId, type, 1);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Staff Pick has been successfully created");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/staff_picks");

                return;
            }
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
        }

        if (client.get().contains("delete") && client.get().contains("type")) {
            pickId = client.get().getInt("delete");
            type = client.get().getString("type");

            if (StringUtils.isNumeric(client.get().getString("delete")) && client.get().getInt("delete") > 0) {
                HousekeepingPromotionDao.deletePickReco(pickId, type, 1);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Staff Pick has been successfully deleted");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/staff_picks");

                return;

            }
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
        }

        if (client.get().contains("edit") && client.get().contains("type")) {
            pickId = client.get().getInt("edit");
            type = client.get().getString("type");

            List<Map<String, Object>> staffPicksListRoomGroupEdit = HousekeepingPromotionDao.EditPickReco(pickId, type, 1);

            for (Map<String, Object> staffPickEdit : staffPicksListRoomGroupEdit) {
                int pickIdRoomGroup = (int) staffPickEdit.get("ID");
                String typeRoomGroup = (String) staffPickEdit.get("type");

                if ("GROUP".equals(typeRoomGroup)) {
                    Group group = GroupDao.getGroup(pickIdRoomGroup);

                    staffPickEdit.put("name", group.getName());
                    staffPickEdit.put("description", group.getDescription());
                    staffPickEdit.put("owner", PlayerDao.getDetails(group.getOwnerId()).getName());
                    staffPickEdit.put("id", group.getId());
                    staffPickEdit.put("pickId", pickIdRoomGroup);
                    staffPickEdit.put("type", typeRoomGroup);
                }
            }

            tpl.set("editingPick", true);
            tpl.set("EditStaffPick", staffPicksListRoomGroupEdit);
            client.session().set("editSession", pickId);
            client.session().set("typeSession", type);
        } else {
            tpl.set("editingPick", false);
            client.session().set("editSession", pickId);
            client.session().set("typeSession", type);
        }

        if (client.post().getString("sid").equals("7")) {
            pickId = client.session().getInt("editSession");
            type = client.session().getString("typeSession");

            String pickIdSave = client.post().getString("IdSave");
            String typeSave = client.post().getString("typeSave");
            int isPickedSave = client.post().getInt("isPickedSave");

            if (StringUtils.isNumeric(client.post().getString("IdSave")) && client.post().getInt("IdSave") > 0) {
                HousekeepingPromotionDao.SavePickReco(pickIdSave, typeSave, isPickedSave, pickId, type, 1);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Staff Pick has been successfully saved");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/staff_picks");

                return;
            }

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Pick ID");
        }

        List<Map<String, Object>> staffPicksListRoomGroup = HousekeepingPromotionDao.getAllPickReco(1);

        for (Map<String, Object> staffPick : staffPicksListRoomGroup) {
            int pickIdRoomGroup = (int) staffPick.get("ID");
            String typeRoomGroup = (String) staffPick.get("type");

            if ("GROUP".equals(typeRoomGroup)) {
                Group group = GroupDao.getGroup(pickIdRoomGroup);

                staffPick.put("groupName", group.getName());
                staffPick.put("groupDescription", group.getDescription());
                staffPick.put("groupOwner", PlayerDao.getDetails(group.getOwnerId()).getName());
                staffPick.put("groupId", group.getId());
                staffPick.put("pickId", pickIdRoomGroup);
                staffPick.put("type", typeRoomGroup);
            }
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Staff Picks");
        tpl.set("StaffPicksList", staffPicksListRoomGroup);
        tpl.set("pickId", pickId);
        tpl.set("type", type);
        tpl.render();

        client.session().delete("alertMessage");
    }
}
