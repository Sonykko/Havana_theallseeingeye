package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPickRecoDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.HousekeepingPickReco;
import org.alexdev.http.game.housekeeping.HousekeepingPickRecoDTO;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
            createRecommended(client, playerDetails);
            return;
        }

        if (client.get().contains("delete")) {
            deleteRecommended(client, playerDetails);
            return;

        }

        if (client.get().contains("edit")) {
            editRecommended(client, tpl);
        } else {
            tpl.set("editingReco", false);
            client.session().set("editSession", recoId);
        }

        if (client.post().getString("sid").equals("7")) {
            saveRecommended(client, playerDetails);
            return;
        }

        List<HousekeepingPickRecoDTO> recommendedList = new ArrayList<>();
        List<HousekeepingPickReco> originalList = HousekeepingPickRecoDao.getAllPickReco(0);

        for (HousekeepingPickReco recommended : originalList) {
            Group group = GroupDao.getGroup(recommended.getRecommendedId());

            if (group == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Recommended ID not exists");
                client.redirect(getRecommendedPath());
                return;
            }

            HousekeepingPickRecoDTO dto = new HousekeepingPickRecoDTO();

            dto.setHousekeepingPickReco(recommended);
            dto.setPickRecoId(group.getId());
            dto.setGroupName(group.getName());
            dto.setGroupDescription(group.getDescription());
            dto.setGroupOwner(PlayerDao.getDetails(group.getOwnerId()).getName());
            dto.setGroupId(group.getId());
            dto.setGroupImage(group.getBadge());
            dto.setIsPicked(recommended.getIsStaffPick());

            recommendedList.add(dto);
        }

        tpl.set("pageName", "Recommended");
        tpl.set("RecommendedList", recommendedList);
        tpl.set("recoId", recoId);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getRecommendedPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/recommended";
    }

    public static void createRecommended (WebConnection client, PlayerDetails playerDetails) {
        String create = client.post().getString("create");

        if (StringUtils.isEmpty(create) || !StringUtils.isNumeric(create)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
            client.redirect(getRecommendedPath());
            return;
        }

        long recoId = 0;
        recoId = Long.parseLong(create);

        Group group = GroupDao.getGroup((int) recoId);

        if (group == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The group not exists");
            client.redirect(getRecommendedPath());
            return;
        }

        var pickReco = HousekeepingPickRecoDao.getPickReco((int) recoId, "GROUP", 0);

        if (pickReco != null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Recommended ID is already a Recommended group");
            client.redirect(getRecommendedPath());
            return;
        }

        HousekeepingPickRecoDao.createPickReco((int) recoId, "GROUP", 0);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created Recommended group with the ID " + recoId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Recommended has been successfully created");
        client.redirect(getRecommendedPath());
    }

    public static void deleteRecommended (WebConnection client, PlayerDetails playerDetails) {
        String delete = client.get().getString("delete");

        if (StringUtils.isEmpty(delete) || !StringUtils.isNumeric(delete)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
            client.redirect(getRecommendedPath());
            return;
        }

        long recoId = 0;
        recoId = client.get().getInt("delete");

        Group group = GroupDao.getGroup((int) recoId);
        var pickReco = HousekeepingPickRecoDao.getPickReco((int) recoId, "GROUP", 0);

        if (group == null || pickReco == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Recommended ID not exists");
            client.redirect(getRecommendedPath());
            return;
        }

        HousekeepingPickRecoDao.deletePickReco(pickReco.getRecommendedId(), "GROUP", 0);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted Recommended group with the ID " + recoId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Recommended has been successfully deleted");
        client.redirect(getRecommendedPath());
    }

    public static void editRecommended (WebConnection client, Template tpl) {
        String edit = client.get().getString("edit");

        if (StringUtils.isEmpty(edit) || !StringUtils.isNumeric(edit)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
            client.redirect(getRecommendedPath());
            return;
        }

        long recoId = 0;
        recoId = client.get().getInt("edit");

        Group group = GroupDao.getGroup((int) recoId);
        var recommendedEdit = HousekeepingPickRecoDao.getPickReco((int) recoId, "GROUP", 0);

        if (group == null || recommendedEdit == null) {
            tpl.set("editingReco", false);
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Recommended ID not exists");
            client.redirect(getRecommendedPath());
            return;
        }

        HousekeepingPickRecoDTO dto = new HousekeepingPickRecoDTO();

        dto.setHousekeepingPickReco(recommendedEdit);
        dto.setGroupName(group.getName());
        dto.setGroupDescription(group.getDescription());
        dto.setGroupOwner(PlayerDao.getDetails(group.getOwnerId()).getName());
        dto.setGroupId(group.getId());
        dto.setGroupImage(group.getBadge());
        dto.setIsPicked(recommendedEdit.getIsStaffPick());

        tpl.set("editingReco", true);
        tpl.set("RecommendedEditList", dto);
        client.session().set("editSession", recoId);
    }

    public static void saveRecommended (WebConnection client, PlayerDetails playerDetails) {
        String save = client.post().getString("IdSave");

        if (StringUtils.isEmpty(save) || !StringUtils.isNumeric(save)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Recommended ID");
            client.redirect(getRecommendedPath());
            return;
        }

        long recoId = 0;
        recoId = client.session().getInt("editSession");

        String recoIdSave = client.post().getString("IdSave");
        long recoIdSaveInt = Integer.parseInt(recoIdSave);
        int setStaffPick = client.post().getInt("setStaffPick");

        Group group = GroupDao.getGroup((int) recoId);
        Group groupSave = GroupDao.getGroup((int) recoIdSaveInt);

        var checkIsPic = HousekeepingPickRecoDao.getPickReco((int) recoIdSaveInt, "GROUP", 0);
        var chechkIsReco = HousekeepingPickRecoDao.getPickReco((int) recoIdSaveInt, "GROUP", 1);

        if (group == null || groupSave == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Recommended ID not exists");
            client.redirect(getRecommendedPath());
            return;
        }

        if (recoId != recoIdSaveInt && checkIsPic != null || setStaffPick == 1 && chechkIsReco != null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Recommended ID is already a Recommended group or Staff Pick");
            client.redirect(getRecommendedPath() + "?edit=" + recoId);
            return;
        }

        HousekeepingPickRecoDao.SavePickReco(recoIdSave, "GROUP", setStaffPick, (int) recoId, "GROUP", 0);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited Recommended group with the ID " + recoId + " (anterior), " + recoIdSave + "(posterior). URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Recommended has been successfully saved");
        client.redirect(getRecommendedPath());
    }
}
