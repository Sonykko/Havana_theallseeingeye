package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCFHTopicsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRoomKickDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

public class HousekeepingRoomKickRCONController {
    public static void roomKickRCON(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/room_kick");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String sortBy = "id";

        if (client.get().contains("sort")) {
            if (client.get().getString("sort").equals("user") ||
                    client.get().getString("sort").equals("id")) {
                sortBy = client.get().getString("sort");
            }
        }

        String action = client.post().getString("action");

        if (client.post().queries().size() > 0) {
            String roomId = client.post().getString("roomId");
            String commonMessage = client.post().getString("commonMessage");
            String customMessage = client.post().getString("customMessage");
            String alertMessage = customMessage != null && !customMessage.isEmpty() ? customMessage : commonMessage;
            String defaultMessage = GameConfiguration.getInstance().getString("rcon.kick.message");
            String finalMessage = customMessage.isEmpty() && commonMessage.isEmpty() ? defaultMessage : alertMessage;
            boolean unacceptable = client.post().getBoolean("unacceptable");
            boolean roomLock = client.post().getBoolean("roomLock");

            if (roomId.length() > 19 || roomId.isEmpty() || !StringUtils.isNumeric(roomId)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid room ID");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
                return;
            }

            long roomIdInt = Long.parseLong(roomId);

            Room room = RoomDao.getRoomById((int) roomIdInt);

            if (room == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The room does not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
                return;
            }

            if (room.isPublicRoom()) {
                unacceptable = false;
                roomLock = false;
            }

            String actionType = null;

            if ("roomKick".equals(action)) {
                actionType = "kick";
            } else if ("roomAlert".equals(action)) {
                actionType = "alert";
            }

            if (actionType.equals("alert") && finalMessage.equals(defaultMessage)) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid alert message");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
                return;
            }

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/api/room.kick?roomId=" + roomId + "&alertRoomKick=" + finalMessage + "&action=" + actionType + "&unacceptable=" + unacceptable + "&roomLock=" + roomLock);
            return;

        }

        tpl.set("pageName", "Remote room alerting & kicking");
        tpl.set("CFHTopics", HousekeepingCFHTopicsDao.getCFHTopics());
        tpl.set("remoteRoomKickLogs", HousekeepingRoomKickDao.RemoteRoomKicksLogs(currentPage, sortBy));
        tpl.set("nextremoteRoomKickLogs", HousekeepingRoomKickDao.RemoteRoomKicksLogs(currentPage + 1, sortBy));
        tpl.set("previousremoteRoomKickLogs", HousekeepingRoomKickDao.RemoteRoomKicksLogs(currentPage - 1, sortBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}
