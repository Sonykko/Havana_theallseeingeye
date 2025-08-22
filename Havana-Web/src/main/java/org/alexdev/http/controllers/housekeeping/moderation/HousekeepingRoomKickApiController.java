package org.alexdev.http.controllers.housekeeping.moderation;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCommandsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.MessageEncoderUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;

public class HousekeepingRoomKickApiController {
    public static void roomKick(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String moderator = playerDetails.getName();
        String roomKick = client.get().getString("roomId");
        String message = client.get().getString("alertRoomKick");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);
        String action = client.get().getString("action");

        int roomId = NumberUtils.isParsable(roomKick) ? Integer.parseInt(roomKick) : 0;
        var room = RoomDao.getRoomById(roomId);

        if (room == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The room does not exists");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
            return;
        }

        boolean unacceptable = !room.isPublicRoom() && client.get().getBoolean("unacceptable");
        boolean roomLock = !room.isPublicRoom() && client.get().getBoolean("roomLock");
        String unacceptableValue = GameConfiguration.getInstance().getString("rcon.room.unacceptable.name");
        String unacceptableDescValue = GameConfiguration.getInstance().getString("rcon.room.unacceptable.desc");
        String unacceptableAlertText = unacceptable ? "and the room has been set as Inappropriate" : "";
        String roomLockText = roomLock ? "and closed with doorbell" : "";

        try {
            RconUtil.sendCommand(RconHeader.MOD_ROOM_KICK, new HashMap<>() {{
                //put("moderatorRoomKick", moderator);
                put("roomId", roomKick);
                put("alertRoomKick", messageEncoded);
                put("action", action);
                put("unacceptable", unacceptable);
                put("unacceptableValue", unacceptableValue);
                put("unacceptableDescValue", unacceptableDescValue);
                put("roomLock", roomLock);

            }});

            String type = action.equals("kick") ? "REMOTE_ROOM_KICK" : "REMOTE_ROOM_ALERT";

            boolean dbInsertSuccess = HousekeepingCommandsDao.insertRconLog(type, roomKick, moderator, message);

            if (dbInsertSuccess) {
                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The " + action + " has been sent and logged in the database " + unacceptableAlertText + " " + roomLockText);
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Error inserting the " + action + " into the database");
            }
        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending the " + action + ": " + e.getMessage());
        }

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/room_kick");
    }
}
