package org.alexdev.http.controllers.housekeeping.moderation;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.moderation.actions.ModeratorBanUserAction;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPlayerDao;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.alexdev.http.util.housekeeping.ModerationApiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingBanApiController {
    public static void superban(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        var playerDetails = PlayerDao.getDetails(client.get().getString("username"));

        if (playerDetails != null) {
            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String message = GameConfiguration.getInstance().getString("rcon.superban.message");

            if (playerDetails.getId() == banningPlayerDetails.getId()) {
                client.send("Can't superban yourself.");
                return;
            }

            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, message, "", playerDetails.getName(), 999999999, true, true));
            return;
        }

        client.send("User doesn't exist");
    }

    public static void banuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        String redirect =  client.get().getString("redirect");
        if (redirect.isEmpty()) {
            redirect = "bans_kicks";
        }

        var playerDetails = PlayerDao.getDetails(client.get().getString("username"));

        if (playerDetails != null) {
            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String alertMessage = client.get().getString("alertMessage");
            String notes = client.get().getString("notes");
            int banSeconds = client.get().getInt("banSeconds");
            boolean doBanMachine = client.get().getBoolean("doBanMachine");
            boolean doBanIP = client.get().getBoolean("doBanIP");

            if (playerDetails.getId() == banningPlayerDetails.getId()) {
                client.send("Can't ban yourself.");
                return;
            }

            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, alertMessage, notes, playerDetails.getName(), banSeconds, doBanMachine, doBanIP));

            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The user " + playerDetails.getName() + " has been banned.");
            return;
        }

        client.send("User doesn't exist");
    }

    public static void massBanuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int banningId1 = client.session().getInt("user.id");
        PlayerDetails staffDetails = PlayerManager.getInstance().getPlayerData(banningId1);

        Map<String, String> params = client.get().getValues();
        String users = params.get("usernames");
        List<String> usernames = ModerationApiUtil.splitUsernames(users);

        for (String username : usernames) {
            String redirect =  client.get().getString("redirect");
            if (redirect.isEmpty()) {
                redirect = "mass_ban";
            }

            var playerDetails = PlayerDao.getDetails(username);

            if (playerDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user " + username + " does not exist.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
                return;
            }

            if (playerDetails.getId() == staffDetails.getId()) {
                client.session().set("alertColour", "warning");
                client.session().set("alertMessage", "Can't mass ban yourself.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
                return;
            }

            RconUtil.sendCommand(RconHeader.DISCONNECT_USER, new HashMap<>() {{
                put("userId", playerDetails.getId());
            }});

            int banningId = client.session().getInt("user.id");
            var banningPlayerDetails = PlayerDao.getDetails(banningId);
            String alertMessage = client.get().getString("alertMessage");
            String notes = client.get().getString("notes");
            int banSeconds = client.get().getInt("banSeconds");
            boolean doBanMachine = client.get().getBoolean("doBanMachine");
            boolean doBanIP = client.get().getBoolean("doBanIP");

            //ModerationDao.addLog(ModerationActionType.ALERT_USER, banningPlayerDetails.getId(), playerDetails.getId(), "Banned for breaking the HabboWay", "");
            client.send(ModeratorBanUserAction.ban(banningPlayerDetails, alertMessage, notes, playerDetails.getName(), banSeconds, doBanMachine, doBanIP));

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The users " + users + " has been banned.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/" + redirect);
            return;
        }
    }

    public static void massUnbanuser(WebConnection client) {
        // If they are logged in, send them to the /me page
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.send("");
        }

        int banningId = client.session().getInt("user.id");
        PlayerDetails staffDetails = PlayerManager.getInstance().getPlayerData(banningId);

        Map<String, String> params = client.get().getValues();
        String users = params.get("usernames");
        List<String> usernames = ModerationApiUtil.splitUsernames(users);

        for (String username : usernames) {
            var playerDetails = PlayerDao.getDetails(username);

            if (playerDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user " + username + " does not exist.");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_unban");
                return;
            }

            HousekeepingPlayerDao.unbanUser(playerDetails.getId());

            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", staffDetails.getId(), staffDetails.getName(), "Ha desbaneado al " + GameConfiguration.getInstance().getString("site.name") + " '" + username + " (id: " + playerDetails.getId() + ")'. URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The users " + users + " has been unbanned.");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/mass_unban");
            return;
        }
    }
}
