package org.alexdev.http.controllers.housekeeping.moderation;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.groups.Group;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.GroupUtil;
import org.alexdev.http.util.HtmlUtil;

public class HousekeepingGroupsApiController {
    public static void showBadgeEditor(WebConnection client) {
        var playerDetails = PlayerDao.getDetails(client.session().getInt("user.id"));

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/groups/show_badge_editor");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        int groupId = client.post().getInt("groupId");

        Group group = GroupDao.getGroup(groupId);

        if (group == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't edit group badge cause group not exists");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/group_admin");
            return;
        }

        tpl.set("group", group);
        tpl.render();
    }

    public static void updateGroupBadge(WebConnection client) {
        var playerDetails = PlayerDao.getDetails(client.session().getInt("user.id"));

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int groupId = client.post().getInt("groupId");
        String badge = HtmlUtil.removeHtmlTags(client.post().getString("code"));

        Group group = GroupDao.getGroup(groupId);

        if (group == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Can't save group badge cause group not exists");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/group_admin");
            return;
        }

        group.setBadge(badge.replaceAll("[^a-zA-Z0-9]", ""));
        group.saveBadge();

        GroupUtil.refreshGroup(groupId);

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "Successfully update the badge of the group with the ID " + groupId);
        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/group_admin");
    }
}
