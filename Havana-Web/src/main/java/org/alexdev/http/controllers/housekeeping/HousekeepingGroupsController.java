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
import org.alexdev.http.dao.housekeeping.HousekeepingGroupsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.GroupUtil;
import org.alexdev.http.util.HtmlUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class HousekeepingGroupsController {
    public static void group_admin (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/group_admin");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        String query = client.post().getString("searchStr");

        List<Group> searchGroups = HousekeepingGroupsDao.searchGroups(query);
        List<Group> searchGroupDetails = new ArrayList<>();

        if ("searchGroup".equals(action)) {
            if (StringUtils.isEmpty(query)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid search value");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/group_admin");
                return;
            }

            for (Group group : searchGroups) {
                int groupId = group.getId();

                Group detailedGroup = GroupDao.getGroup(groupId);

                searchGroupDetails.add(detailedGroup);
            }

            if (searchGroups.isEmpty()) {
                tpl.set("searchEmpty", true);
            } else {
                tpl.set("searchEmpty", false);
            }
        }

        tpl.set("query", query);
        tpl.set("searchGroupDetails", searchGroupDetails);

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        List<Group> allGroups = HousekeepingGroupsDao.getAllGroups(currentPage);
        List<Group> allGroupDetails = new ArrayList<>();

        for (Group group : allGroups) {
            int groupId = group.getId();

            Group detailedGroup = GroupDao.getGroup(groupId);

            allGroupDetails.add(detailedGroup);
        }

        tpl.set("allGroupDetails", allGroupDetails);
        tpl.set("nextAllGroupDetails", HousekeepingGroupsDao.getAllGroups(currentPage + 1));
        tpl.set("previousAllGroupDetails", HousekeepingGroupsDao.getAllGroups(currentPage - 1));

        if ("groupSave".equals(action)) {
            String groupId = client.post().getString("groupId");

            Group group = GroupDao.getGroup(Integer.parseInt(groupId));

            if (group == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Can't save the group cause not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/group_admin");
                return;
            }

            String name = HtmlUtil.removeHtmlTags(client.post().getString("groupName"));
            String description = HtmlUtil.removeHtmlTags(client.post().getString("groupDesc"));
            String url = client.post().getString("groupAlias").replaceAll("[^a-zA-Z0-9]", "");

            if (url.length() > 30) {
                url = url.substring(0, 30);
            }

            if (name.length() > 30) {
                name = name.substring(0, 30);
            }

            if (description.length() > 255) {
                description = description.substring(0, 255);
            }

            int groupType = client.post().getInt("groupType");
            int roomId = 0;

            try {
                roomId = client.post().getString("groupRoom").length() > 0 ? client.post().getInt("groupRoom") : 0;
            } catch (Exception ex) {

            }
            if (groupType < 0 || groupType > 3) {
                groupType = 0;
            }

            if (roomId < 0) {
                roomId = 0;
            }

            group.setName(name);
            group.setDescription(description);

            if (group.getGroupType() != 3) {
                group.setGroupType(groupType);
            }

            if (group.getAlias() == null || group.getAlias().isBlank()) {
                group.setAlias(null);

                if (!url.isBlank()) {
                    boolean existing = GroupDao.hasGroupByAlias(url);

                    if (!existing) {
                        group.setAlias(url);
                    }
                }
            }

            RoomDao.saveGroupId(group.getRoomId(), 0);

            if (roomId > 0) {
                Room room = RoomDao.getRoomById(roomId);

                if (room == null) {
                    roomId = 0;
                } else {
                    RoomDao.saveGroupId(roomId, Integer.parseInt(groupId));
                }
            }

            group.setRoomId(roomId);
            group.save();

            GroupUtil.refreshGroup(Integer.parseInt(groupId));

            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Updated the group with the ID " + groupId + ". URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "Successfully update the group with the ID " + groupId);
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/group_admin");
            return;
        }

        tpl.set("pageName", "Group admin");
        tpl.set("allRanks", HousekeepingPlayerDao.getAllRanks());
        tpl.set("page", currentPage);
        tpl.set("ruffleActive", true);
        tpl.render();

        client.session().delete("alertMessage");
    }
}