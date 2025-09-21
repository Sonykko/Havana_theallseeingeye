package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingChatLogDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.ChatLog;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.HousekeepingMessengerChatLog;
import org.alexdev.http.game.housekeeping.HousekeepingRoomChatLog;
import org.alexdev.http.util.SessionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HousekeepingChatLogsController {

    /**
     * Handle the /housekeeping URI request
     *
     * @param client the connection
     */
    public static void roomchatlogs(WebConnection client) { // Añadir BOLEAN para mostrar todos los chats chatlogs de manera predifinida o buscar por nombre, id o dueño o etc...
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/room_chatlogs");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().queries().size() > 0) {
            String[] fieldCheck = new String[]{"searchField", "searchQuery", "searchType" };

            for (String field : fieldCheck) {
                if (client.post().contains(field) && client.post().getString(field).length() > 0) {
                    continue;
                }

                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "You need to enter all fields");
                tpl.render();

                // Delete alert after it's been rendered
                client.session().delete("alertMessage");
                return;
            }

            String field = client.post().getString("searchField");
            String input = client.post().getString("searchQuery");
            String type = client.post().getString("searchType");

            List<String> whitelistColumns = new ArrayList<>();
            whitelistColumns.add("id");
            whitelistColumns.add("message");
            whitelistColumns.add("room_id");
            whitelistColumns.add("room_name");

            List<HousekeepingRoomChatLog> chatLogs = null;

            if (whitelistColumns.contains(field)) {
                chatLogs = HousekeepingChatLogDao.searchRoomChatLogs(type, field, input);
            } else {
                chatLogs = new ArrayList<>();
            }

            if (!(chatLogs.size() > 0)) {
                tpl.set("noResults", true);
            }

            tpl.set("searchChatlogs", chatLogs);
            tpl.render();
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        String sortBy = "timestamp";

        if (client.get().contains("sort")) {
            if (client.get().getString("sort").equals("room_id") ||
                    client.get().getString("sort").equals("timestamp")) {
                sortBy = client.get().getString("sort");
            }
        }

        tpl.set("pageName", "Room Chatlogs");
        tpl.set("chatlogs", HousekeepingChatLogDao.getAllRoomChatlogs(currentPage, sortBy));
        tpl.set("nextChatlogs", HousekeepingChatLogDao.getAllRoomChatlogs(currentPage + 1, sortBy));
        tpl.set("previousChatlogs", HousekeepingChatLogDao.getAllRoomChatlogs(currentPage - 1, sortBy));
        tpl.set("page", currentPage);
        tpl.set("sortBy", sortBy);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void userchatlogs(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/user_chatlogs");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "bans")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        List<ChatLog> userChatlogs = new ArrayList<>();
        String userId1Param = client.get().getString("id1");
        String userId2Param = client.get().getString("id2");

        if (!userId1Param.isEmpty() && !userId2Param.isEmpty()) {
            try {
                int userId1 = Integer.parseInt(userId1Param);
                int userId2 = Integer.parseInt(userId2Param);

                userChatlogs.addAll(HousekeepingChatLogDao.getConversationsBetweenUsers(userId1, userId2));
            } catch (NumberFormatException e) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please provide valid user IDs for the conversation search.");
            }
        } else {
            String userIdParam = client.get().getString("chatId");
            if (!userIdParam.isEmpty()) {
                try {
                    int userId = Integer.parseInt(userIdParam);

                    List<HousekeepingRoomChatLog> chatLogs = HousekeepingChatLogDao.getChatlogsByUserId(userId);
                    List<HousekeepingMessengerChatLog> messengerMessages = HousekeepingChatLogDao.getMessengerMessagesByUserId(userId);

                    for (HousekeepingRoomChatLog chatlog : chatLogs) {
                        chatlog.setLogType("Chatlog");
                    }

                    for (HousekeepingMessengerChatLog message : messengerMessages) {
                        message.setLogType("MessengerMessage");
                    }

                    userChatlogs.addAll(chatLogs);
                    userChatlogs.addAll(messengerMessages);
                    Collections.sort(userChatlogs, (a, b) -> Long.compare(b.getDate(), a.getDate()));

                    if (userChatlogs.isEmpty()) {
                        client.session().set("alertColour", "danger");
                        client.session().set("alertMessage", "The user with the ID " + userIdParam + " does not have any chatlog.");
                    }
                } catch (NumberFormatException e) {
                    client.session().set("alertColour", "danger");
                    client.session().set("alertMessage", "The ID " + userIdParam + " is not valid. Please provide a valid ID or two valid user IDs as 'id1' and 'id2' in the URL.");
                }
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please provide valid user IDs as 'id1' and 'id2', or a valid user ID as 'chatId' in the URL.");
            }
        }

        tpl.set("userChatlogs", userChatlogs);
        tpl.set("pageName", "User Chatlogs");
        tpl.render();

        // Elimina el mensaje de alerta después de renderizarlo
        client.session().delete("alertMessage");
    }
}