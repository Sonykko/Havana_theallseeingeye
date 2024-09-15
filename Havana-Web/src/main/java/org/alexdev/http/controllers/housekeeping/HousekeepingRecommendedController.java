package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPromotionDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class HousekeepingRecommendedController {
    public static void recommended (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/recommended");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "articles/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        long recoId = 0;

        if (client.post().contains("create")) {
            String create = client.post().getString("create");

            if (StringUtils.isEmpty(create) || !StringUtils.isNumeric(create)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Recommended ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            recoId = Long.parseLong(create);

            Group group = GroupDao.getGroup((int) recoId);

            if (group == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The group not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            List<Map<String, Object>> checkPickReco = HousekeepingPromotionDao.EditPickReco((int) recoId, "GROUP", 0);

            if (!checkPickReco.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID is already a Recommended group");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            HousekeepingPromotionDao.createPickReco((int) recoId, "GROUP", 0);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created Recommended group with the ID " + recoId + ". URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Recommended has been successfully created");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
            return;
        }

        if (client.get().contains("delete")) {
            String delete = client.get().getString("delete");

            if (StringUtils.isEmpty(delete) || !StringUtils.isNumeric(delete)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Recommended ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            recoId = client.get().getInt("delete");

            Group group = GroupDao.getGroup((int) recoId);
            List<Map<String, Object>> checkPickReco = HousekeepingPromotionDao.EditPickReco((int) recoId, "GROUP", 0);

            if (group == null || checkPickReco.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            HousekeepingPromotionDao.deletePickReco((int) recoId, "GROUP", 0);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted Recommended group with the ID " + recoId + ". URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Recommended has been successfully deleted");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
            return;

        }

        if (client.get().contains("edit")) {
            String edit = client.get().getString("edit");

            if (StringUtils.isEmpty(edit) || !StringUtils.isNumeric(edit)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Recommended ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            recoId = client.get().getInt("edit");

            Group groupEdit = GroupDao.getGroup((int) recoId);
            List<Map<String, Object>> checkPickReco = HousekeepingPromotionDao.EditPickReco((int) recoId, "GROUP", 0);

            if (groupEdit == null || checkPickReco.isEmpty()) {
                tpl.set("editingReco", false);
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            List<Map<String, Object>> recommendedEditList = HousekeepingPromotionDao.EditPickReco((int) recoId, "GROUP", 0);

            for (Map<String, Object> recommendedEdit : recommendedEditList) {
                int recommendedId = (int) recommendedEdit.get("ID");
                String typeRoomGroup = (String) recommendedEdit.get("type");
                int isPicked = (int) recommendedEdit.get("isPicked");

                Group group = GroupDao.getGroup(recommendedId);

                recommendedEdit.put("groupName", group.getName());
                recommendedEdit.put("groupDescription", group.getDescription());
                recommendedEdit.put("groupOwner", PlayerDao.getDetails(group.getOwnerId()).getName());
                recommendedEdit.put("groupId", group.getId());
                recommendedEdit.put("groupImage", group.getBadge());
                recommendedEdit.put("type", typeRoomGroup);
                recommendedEdit.put("isPicked", isPicked);

            }

            tpl.set("editingReco", true);
            tpl.set("RecommendedEditList", recommendedEditList);
            client.session().set("editSession", recoId);
        } else {
            tpl.set("editingReco", false);
            client.session().set("editSession", recoId);
        }

        if (client.post().getString("sid").equals("7")) {
            String save = client.post().getString("IdSave");

            if (StringUtils.isEmpty(save) || !StringUtils.isNumeric(save)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Recommended ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            recoId = client.session().getInt("editSession");

            String recoIdSave = client.post().getString("IdSave");
            long recoIdSaveInt = Integer.parseInt(recoIdSave);
            int setStaffPick = client.post().getInt("setStaffPick");

            Group group = GroupDao.getGroup((int) recoId);
            Group groupSave = GroupDao.getGroup((int) recoIdSaveInt);

            List<Map<String, Object>> checkIsPic = HousekeepingPromotionDao.EditPickReco((int) recoIdSaveInt, "GROUP", 0);
            List<Map<String, Object>> chechkIsReco = HousekeepingPromotionDao.EditPickReco((int) recoIdSaveInt, "GROUP", 1);

            if (group == null || groupSave == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
                return;
            }

            if (recoId != recoIdSaveInt && !checkIsPic.isEmpty() || setStaffPick == 1 && !chechkIsReco.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID is already a Recommended group or Staff Pick");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended?edit=" + recoId);
                return;
            }

            HousekeepingPromotionDao.SavePickReco(recoIdSave, "GROUP", setStaffPick, (int) recoId, "GROUP", 0);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited Recommended group with the ID " + recoId + " (anterior), " + recoIdSave + "(posterior). URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Recommended has been successfully saved");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");
            return;
        }

        List<Map<String, Object>> recommendedList = HousekeepingPromotionDao.getAllPickReco(0);

        for (Map<String, Object> recommended : recommendedList) {
            int recommendedId = (int) recommended.get("ID");
            int isPicked = (int) recommended.get("isPicked");

            Group group = GroupDao.getGroup(recommendedId);

            if (group == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID not exists");
            } else {
                recommended.put("groupName", group.getName());
                recommended.put("groupDescription", group.getDescription());
                recommended.put("groupOwner", PlayerDao.getDetails(group.getOwnerId()).getName());
                recommended.put("groupId", group.getId());
                recommended.put("groupImage", group.getBadge());
                recommended.put("isPicked", isPicked);
            }
        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Recommended");
        tpl.set("RecommendedList", recommendedList);
        tpl.set("recoId", recoId);
        tpl.render();

        client.session().delete("alertMessage");
    }
}
