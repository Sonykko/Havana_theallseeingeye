package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.EmailUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class HousekeepingRestoreController {
    public static void restore(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/users_restore");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (!GameConfiguration.getInstance().getBoolean("email.smtp.enable")) {
            client.session().set("alertColour", "warning");
            client.session().set("alertMessage", "Please enable and setup Email SMTP service in order to work this feature properly");
        }

        String event = client.post().getString("event");

        if (event.equals("mass")) {
            massRestore(client, playerDetails);
            return;
        }

        if (event.equals("one")) {
            oneRestore(client, playerDetails);
            return;
        }

        tpl.set("pageName", "Habbo restore tool");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    private static String getRestorePath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/users/restore";
    }

    public static void massRestore (WebConnection client, PlayerDetails playerDetails) {
        String mass = client.post().getString("massRestore");
        String subject = client.post().getString("subject");
        String message = client.post().getString("message");

        if (!GameConfiguration.getInstance().getBoolean("email.smtp.enable")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error restoring the account cause SMTP service is disabled");
            client.redirect(getRestorePath());
            return;
        }

        String[] usernames = mass.split("\\r?\\n");

        for (String userName : usernames) {
            userName = userName.trim();

            var restoreDetails = PlayerDao.getDetails(userName);

            if (restoreDetails == null) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The user does not exist.");
                client.redirect(getRestorePath());
                return;
            }

            if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(message) || !StringUtils.contains(message, "%password%")) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid subject or message.");
                client.redirect(getRestorePath());
                return;
            }

            String newPassword = generateRandomPassword();
            String messagePassword = StringUtils.replace(message, "%password%", newPassword);
            messagePassword = StringUtils.replace(messagePassword, "\n", "<br>");

            EmailUtil.send(client, restoreDetails.getEmail(), subject, EmailUtil.renderRestoreAccount(messagePassword));

            PlayerDao.setPassword(restoreDetails.getId(), PlayerManager.getInstance().createPassword(newPassword));

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "User " + restoreDetails.getName() + ", new pass: " + newPassword);

            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Restored the user " + restoreDetails.getName() + ". URL: " + client.request().uri(), client.getIpAddress());
        }

        client.redirect(getRestorePath());
    }

    public static void oneRestore (WebConnection client, PlayerDetails playerDetails) {
        String userName = client.post().getString("userName");
        String newUserEmail = client.post().getString("newUserEmail");
        String subject = client.post().getString("subject");
        String message = client.post().getString("message");

        if (!GameConfiguration.getInstance().getBoolean("email.smtp.enable")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Error restoring the account cause SMTP service is disabled");
            client.redirect(getRestorePath());
            return;
        }

        var restoreDetails = PlayerDao.getDetails(userName);

        if (restoreDetails == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The user does not exist.");
            client.redirect(getRestorePath());
            return;
        }

        if (!newUserEmail.matches("^[a-z0-9_.-]+@([a-z0-9]+([-]+[a-z0-9]+)*\\.)+[a-z]{2,7}$")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid email.");
            client.redirect(getRestorePath());
            return;
        }

        if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(message) || !StringUtils.contains(message, "%password%")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid subject or message.");
            client.redirect(getRestorePath());
            return;
        }

        String newPassword = generateRandomPassword();
        String messagePassword = StringUtils.replace(message, "%password%", newPassword);
        messagePassword = StringUtils.replace(messagePassword, "\n", "<br>");

        EmailUtil.send(client, newUserEmail, subject, EmailUtil.renderRestoreAccount(messagePassword));

        PlayerDao.setPassword(restoreDetails.getId(), PlayerManager.getInstance().createPassword(newPassword));
        PlayerDao.setEmail(restoreDetails.getId(), newUserEmail);

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "User " + restoreDetails.getName() + ", email: " + newUserEmail + ", new pass: " + newPassword);

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Restored the user " + restoreDetails.getName() + ". URL: " + client.request().uri(), client.getIpAddress());

        client.redirect(getRestorePath());
    }

    private static String generateRandomPassword() {
        int codeLength = 8;
        StringBuilder randomCode = new StringBuilder();

        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());

            char randomChar = characters.charAt(index);

            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }
}