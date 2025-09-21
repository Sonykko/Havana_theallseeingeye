package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingMessengerChatLog;
import org.alexdev.http.game.housekeeping.HousekeepingRoomChatLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingChatLogDao {
    public static List<HousekeepingRoomChatLog> getAllRoomChatlogs(int page, String sortBy) {
        List<HousekeepingRoomChatLog> chatHistoryList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare(
                        "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                                "FROM room_chatlogs " +
                                "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                                "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                                "ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?",
                        sqlConnection
                );
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    chatHistoryList.add(fillRoomChatLog(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return chatHistoryList;
    }

    public static List<HousekeepingRoomChatLog> searchRoomChatLogs(String type, String field, String input) {
        List<HousekeepingRoomChatLog> chatLogs = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            String query = "";

            if (type.equals("contains")) {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " LIKE ?" +
                        "ORDER BY id DESC LIMIT 50";
                input = "%" + input + "%";
            } else if (type.equals("starts_with")) {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " LIKE ?" +
                        "ORDER BY id DESC LIMIT 50";
                input = input + "%";
            } else if (type.equals("ends_with")) {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " LIKE ?" +
                        "ORDER BY id DESC LIMIT 50";
                input = "%" + input;
            } else {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " = ?" +
                        "ORDER BY id DESC LIMIT 50";
            }

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setString(1, input);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chatLogs.add(fillRoomChatLog(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return chatLogs;
    }

    public static List<HousekeepingRoomChatLog> getChatlogsByUserId(int userId) {
        List<HousekeepingRoomChatLog> chatLogs = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            // Query to select chatlogs by user ID
            String query = "SELECT room_chatlogs.*, users.id AS userId, users.username AS username, rooms.name AS room_name " +
                    "FROM room_chatlogs " +
                    "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                    "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                    "WHERE room_chatlogs.user_id = ? " +
                    "LIMIT 100";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chatLogs.add(fillRoomChatLog(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return chatLogs;
    }

    public static List<HousekeepingMessengerChatLog> getMessengerMessagesByUserId(int userId) {
        List<HousekeepingMessengerChatLog> messengerMessages = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "SELECT 'Messenger Message' AS log_type, messenger_messages.id AS id, " +
                    "messenger_messages.sender_id AS userId, users.username AS username, " +
                    "messenger_messages.body AS message, messenger_messages.receiver_id AS friendId, " +
                    "friend_user.username AS friendName, " +
                    "messenger_messages.date AS timestamp FROM messenger_messages " +
                    "INNER JOIN users ON messenger_messages.sender_id = users.id " +
                    "LEFT JOIN users AS friend_user ON messenger_messages.receiver_id = friend_user.id " +
                    "WHERE messenger_messages.sender_id = ? " +
                    "LIMIT 100";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                messengerMessages.add(fillMessengerChatLog(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return messengerMessages;
    }

    public static List<HousekeepingMessengerChatLog> getConversationsBetweenUsers(int userId1, int userId2) {
        List<HousekeepingMessengerChatLog> conversations = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "SELECT 'Conversation' AS log_type, messenger_messages.id, sender_id AS userId, " +
                    "users.username AS username, body AS message, receiver_id AS friendId, " +
                    "friend_user.username AS friendName, date AS timestamp " +
                    "FROM messenger_messages " +
                    "INNER JOIN users ON sender_id = users.id " +
                    "LEFT JOIN users AS friend_user ON receiver_id = friend_user.id " +
                    "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                    "ORDER BY date DESC";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.setInt(3, userId2);
            preparedStatement.setInt(4, userId1);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                conversations.add(fillMessengerChatLog(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return conversations;
    }

    private static HousekeepingRoomChatLog fillRoomChatLog(ResultSet resultSet) throws Exception {
        return new HousekeepingRoomChatLog(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("message"),
                resultSet.getString("room_name"),
                resultSet.getString("room_id"),
                resultSet.getLong("timestamp")
        );
    }

    private static HousekeepingMessengerChatLog fillMessengerChatLog(ResultSet resultSet) throws Exception {
        return new HousekeepingMessengerChatLog(
                resultSet.getString("log_type"),
                resultSet.getInt("id"),
                resultSet.getInt("userId"),
                resultSet.getString("username"),
                resultSet.getString("friendId"),
                resultSet.getString("friendName"),
                resultSet.getString("body"),
                resultSet.getLong("date")
        );
    }
}
