package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.BadgeDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.HousekeepingUtil;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingRoomBadgesController {
    public static void badges(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/room_badges");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "room_badges")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        try {
            if (client.post().queries().size() > 0) {
                Map<Integer, List<String>> badges = new HashMap<>();

                for (var kvp : client.post().getValues().entrySet()) {
                    String key = kvp.getKey();

                    if (!key.startsWith("roombadge-id-")) {
                        continue;
                    }

                    String values = key.replace("roombadge-id-", "");

                    String[] data = values.split("_");
                    int roomId = client.post().getInt("roomad-" + values + "-roomid");
                    String badgeCode = client.post().getString("roomad-" + values + "-badge");

                    if (!badges.containsKey(roomId)) {
                        badges.put(roomId, new ArrayList<>());
                    }

                    badges.get(roomId).add(badgeCode);
                }

                BadgeDao.updateBadges(badges);
                sendRoomBadgeUpdate();

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "All badge rooms have been saved successfully!");
            }

        } catch (Exception ex) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error occurred, make sure the room ID is a valid number");
        }

        tpl.set("pageName", "Room Badges");
        RoomManager.getInstance();
        tpl.set("roomBadges", RoomManager.getInstance().getRoomEntryBadges());
        tpl.set("util", new HousekeepingUtil());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void delete(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/room_badges");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "room_badges")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        if (client.post().queries().size() > 0) {
            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "All badge rooms have been saved successfully!");
        }

        if (!client.get().contains("id")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "There was no badge selected to delete");
        } else { client.session().set("alertColour", "success");
            client.session().set("alertMessage", "Successfully deleted the badge");

            String[] data = client.get().getString("id").split("_");
            BadgeDao.deleteRoomBadge(data[0], data[1]);
        }

        sendRoomBadgeUpdate();

        tpl.set("pageName", "Room Badges");
        tpl.set("roomBadges", RoomManager.getInstance().getRoomEntryBadges());
        tpl.set("util", new HousekeepingUtil());
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void create(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/room_badges_create");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "room_badges")) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        if (client.post().getValues().size() > 0) {
            try {
                BadgeDao.createEntryBadge(
                        client.post().getInt("roomid"),
                        client.post().getString("badgecode"));

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "Successfully created the room entry badge");

                sendRoomBadgeUpdate();
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/room_badges");

                return;
            } catch (Exception ex) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error occurred, make sure the room ID is a valid number");
            }

        }

        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    private static void sendRoomBadgeUpdate() {
        RoomManager.getInstance().reloadBadges();
        RconUtil.sendCommand(RconHeader.REFRESH_ROOM_BADGES, new HashMap<>());
    }
}
