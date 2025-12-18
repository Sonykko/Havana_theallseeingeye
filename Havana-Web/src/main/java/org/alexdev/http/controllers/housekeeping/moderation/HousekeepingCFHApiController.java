package org.alexdev.http.controllers.housekeeping.moderation;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.MessageEncoderUtil;

import java.util.HashMap;

public class HousekeepingCFHApiController {
    public static void cfhPickUp(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.CFH_PICK, new HashMap<>() {{
                put("cryId", client.get().getString("cryId"));
                put("moderator", playerDetails.getName());

            }});

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error Picking Up the CFH: " + e.getMessage());
        }

        String cryId = client.get().getString("cryId");

        client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_actions?cryId=" + cryId);
    }

    public static void cfhReply(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String cryIdReply = client.get().getString("cryIdReply");
        String message = client.get().getString("messageReply");
        String messageEncoded = MessageEncoderUtil.encodeMessage(message);

        try {
            RconUtil.sendCommand(RconHeader.CFH_REPLY, new HashMap<>() {{
                put("cryIdReply", cryIdReply);
                put("messageReply", messageEncoded);
                put("moderatorReply", playerDetails.getName());

            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Reply has been sent and logged in the database");

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending CFH reply: " + e.getMessage());
        }

        if (HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_logs");
        } else {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_mods");
        }
    }

    public static void cfhBlock(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.CFH_BLOCK, new HashMap<>() {{
                put("cryIdBlock", client.get().getString("cryIdBlock"));
                put("moderatorBlock", playerDetails.getName());

            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Block has been sent and logged in the database");

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending CFH block: " + e.getMessage());
        }

        if (HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_logs");
        } else {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_mods");
        }
    }

    public static void cfhFollow(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int userId = client.session().getInt("user.id");
        PlayerDetails playerDetails = PlayerDao.getDetails(userId);

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            RconUtil.sendCommand(RconHeader.CFH_FOLLOW, new HashMap<>() {{
                put("cryIdFollow", client.get().getString("cryIdFollow"));
                put("moderatorFollow", playerDetails.getName());

            }});

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Follow has been sent and logged in the database");

        } catch (Exception e) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error sending CFH follow: " + e.getMessage());
        }

        if (HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_logs");
        } else {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/cfh_mods");
        }
    }
}
