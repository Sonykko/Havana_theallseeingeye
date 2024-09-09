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

        int recoId = 0;

        if (client.post().contains("create")) {
            if (!StringUtils.isEmpty(client.post().getString("create")) && StringUtils.isNumeric(client.post().getString("create")) && client.post().getInt("create") > 0) {
                recoId = client.post().getInt("create");

                HousekeepingPromotionDao.createPickReco(recoId, "GROUP", 0);
                HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created Recommended group with the ID " + recoId + ". URL: " + client.request().uri(), client.getIpAddress());

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Recommended has been successfully created");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");

                return;
            }
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
        }

        if (client.get().contains("delete")) {
            if (!StringUtils.isEmpty(client.get().getString("delete")) && StringUtils.isNumeric(client.get().getString("delete")) && client.get().getInt("delete") > 0) {
                recoId = client.get().getInt("delete");

                HousekeepingPromotionDao.deletePickReco(recoId, "GROUP", 0);
                HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Recommended group with the ID " + recoId + ". URL: " + client.request().uri(), client.getIpAddress());

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Recommended has been successfully deleted");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");

                return;
            }
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
        }

        if (client.get().contains("edit")) {
            if (!StringUtils.isEmpty(client.get().getString("edit")) && StringUtils.isNumeric(client.get().getString("edit")) && client.get().getInt("edit") > 0) {
                recoId = client.get().getInt("edit");

                List<Map<String, Object>> recommendedEditList = HousekeepingPromotionDao.EditPickReco(recoId, "GROUP", 0);

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
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Recommended ID");
            }
        } else {
            tpl.set("editingReco", false);
            client.session().set("editSession", recoId);
        }

        if (client.post().getString("sid").equals("7")) {
            if (!StringUtils.isEmpty(client.post().getString("IdSave")) && StringUtils.isNumeric(client.post().getString("IdSave")) && client.post().getInt("IdSave") > 0) {

                recoId = client.session().getInt("editSession");

                String recoIdSave = client.post().getString("IdSave");
                String typeSave = client.post().getString("typeSave");
                int isPickedSave = client.post().getInt("isPickedSave");

                //HousekeepingPromotionDao.SaveRecommended(recoIdSave, typeSave, isPicked, recoId);
                HousekeepingPromotionDao.SavePickReco(recoIdSave, typeSave, isPickedSave, recoId, "GROUP", 0);
                HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited Recommended group with the ID " + recoId + " (anterior), " + recoIdSave + "(posterior). URL: " + client.request().uri(), client.getIpAddress());

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Recommended has been successfully saved");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended");

                return;
            }
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
        }

        List<Map<String, Object>> recommendedList = HousekeepingPromotionDao.getAllPickReco(0);

        for (Map<String, Object> recommended : recommendedList) {
            int recommendedId = (int) recommended.get("ID");
            int isPicked = (int) recommended.get("isPicked");

            Group group = GroupDao.getGroup(recommendedId);

            recommended.put("groupName", group.getName());
            recommended.put("groupDescription", group.getDescription());
            recommended.put("groupOwner", PlayerDao.getDetails(group.getOwnerId()).getName());
            recommended.put("groupId", group.getId());
            recommended.put("groupImage", group.getBadge());
            recommended.put("isPicked", isPicked);

        }

        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        tpl.set("pageName", "Recommended");
        tpl.set("RecommendedList", recommendedList);
        tpl.set("recoId", recoId);
        tpl.render();

        client.session().delete("alertMessage");
    }
}
