package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.moderation.ChatMessage;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingCFHDao {

    public static List<Map<String, Object>> getCFHlog(int page, String sortBy) {
        List<Map<String, Object>> CFHList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare(
                        "SELECT cfh_logs.* " +
                                "FROM cfh_logs " + // Cambiar a la tabla cfh_logs
                                "ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?",
                        sqlConnection
                );
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> cfhLog = new HashMap<>();
                    cfhLog.put("username", resultSet.getString("user")); // Cambiar a la columna correspondiente
                    cfhLog.put("message", resultSet.getString("message")); // Cambiar a la columna correspondiente
                    cfhLog.put("roomName", resultSet.getString("room")); // Cambiar a la columna correspondiente
                    cfhLog.put("roomId", resultSet.getInt("room_id")); // Cambiar a la columna correspondiente
                    cfhLog.put("createdTime", resultSet.getString("created_time")); // Cambiar a la columna correspondiente
                    cfhLog.put("status", resultSet.getString("is_deleted")); // Cambiar a la columna correspondiente
                    cfhLog.put("moderator", resultSet.getString("moderator")); // Cambiar a la columna correspondiente
                    cfhLog.put("pickedTime", resultSet.getString("picked_time")); // Cambiar a la columna correspondiente
                    cfhLog.put("cfhId", resultSet.getInt("cfh_id")); // Cambiar a la columna correspondiente

                    CFHList.add(cfhLog);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return CFHList;
    }

    public static List<Map<String, Object>> searchCFHlog(String type, String field, String input) {
        List<Map<String, Object>> CFHListSearch = new ArrayList<>();

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                if (type.equals("contains")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " LIKE ?", sqlConnection);
                    preparedStatement.setString(1, "%" + input + "%");
                } else if (type.equals("starts_with")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " LIKE ?", sqlConnection);
                    preparedStatement.setString(1, input + "%");
                } else if (type.equals("ends_with")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " LIKE ?", sqlConnection);
                    preparedStatement.setString(1, "%" + input);
                } else {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " = ?", sqlConnection);
                    preparedStatement.setString(1, input);
                }

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> cfhLogSearch = new HashMap<>();
                    cfhLogSearch.put("username", resultSet.getString("user")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("message", resultSet.getString("message")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("roomName", resultSet.getString("room")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("roomId", resultSet.getInt("room_id")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("createdTime", resultSet.getString("created_time")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("status", resultSet.getString("is_deleted")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("moderator", resultSet.getString("moderator")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("pickedTime", resultSet.getString("picked_time")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("cfhId", resultSet.getInt("cfh_id")); // Cambiar a la columna correspondiente

                    CFHListSearch.add(cfhLogSearch);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }

        return CFHListSearch;
    }

    public static List<Map<String, Object>> searchCFHlogCON_LIMIT_ROW(String type, String field, String input, int page) {
        List<Map<String, Object>> CFHListSearch = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                if (type.equals("contains")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " LIKE ? LIMIT ? OFFSET ?", sqlConnection);
                    preparedStatement.setString(1, "%" + input + "%");
                    preparedStatement.setInt(2, rows);
                    preparedStatement.setInt(3, nextOffset);
                } else if (type.equals("starts_with")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " LIKE ? LIMIT ? OFFSET ?", sqlConnection);
                    preparedStatement.setString(1, input + "%");
                    preparedStatement.setInt(2, rows);
                    preparedStatement.setInt(3, nextOffset);
                } else if (type.equals("ends_with")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " LIKE ? LIMIT ? OFFSET ?", sqlConnection);
                    preparedStatement.setString(1, "%" + input);
                    preparedStatement.setInt(2, rows);
                    preparedStatement.setInt(3, nextOffset);
                } else {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE " + field + " = ? LIMIT ? OFFSET ?", sqlConnection);
                    preparedStatement.setString(1, input);
                    preparedStatement.setInt(2, rows);
                    preparedStatement.setInt(3, nextOffset);
                }

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> cfhLogSearch = new HashMap<>();
                    cfhLogSearch.put("username", resultSet.getString("user")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("message", resultSet.getString("message")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("roomName", resultSet.getString("room")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("roomId", resultSet.getInt("room_id")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("createdTime", resultSet.getString("created_time")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("status", resultSet.getString("is_deleted")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("moderator", resultSet.getString("moderator")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("pickedTime", resultSet.getString("picked_time")); // Cambiar a la columna correspondiente
                    cfhLogSearch.put("cfhId", resultSet.getInt("cfh_id")); // Cambiar a la columna correspondiente

                    CFHListSearch.add(cfhLogSearch);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return CFHListSearch;
    }

    public static List<Map<String, Object>> searchChatLogs(String type, String field, String input) {
        List<Map<String, Object>> chatLogs = new ArrayList<>();

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
                        "WHERE " + field + " LIKE ?";
                input = "%" + input + "%";
            } else if (type.equals("starts_with")) {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " LIKE ?";
                input = input + "%";
            } else if (type.equals("ends_with")) {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " LIKE ?";
                input = "%" + input;
            } else {
                query = "SELECT room_chatlogs.*, users.username AS username, rooms.name AS room_name " +
                        "FROM room_chatlogs " +
                        "INNER JOIN users ON room_chatlogs.user_id = users.id " +
                        "INNER JOIN rooms ON room_chatlogs.room_id = rooms.id " +
                        "WHERE " + field + " = ?";
            }

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setString(1, input);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> chatLog = new HashMap<>();
                chatLog.put("id", resultSet.getInt("id"));
                chatLog.put("username", resultSet.getString("username"));
                chatLog.put("message", resultSet.getString("message"));
                chatLog.put("roomName", resultSet.getString("room_name"));
                chatLog.put("roomId", resultSet.getInt("room_id"));
                chatLog.put("timestamp", resultSet.getLong("timestamp"));

                chatLogs.add(chatLog);
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

    public static List<ChatMessage> getModChatlog2(int page, String sortBy) {
        List<ChatMessage> chatHistoryList = new ArrayList<>();

        int rows = 50;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            //room_chatlogs (user_id, room_id, timestamp, chat_type, message)

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
                    CHAT_MESSAGE.ChatMessageType chatMessageType = CHAT_MESSAGE.ChatMessageType.CHAT;
                    int chatType = resultSet.getInt("chat_type");

                    if (chatType == 2) {
                        chatMessageType = CHAT_MESSAGE.ChatMessageType.WHISPER;
                    }

                    if (chatType == 1) {
                        chatMessageType = CHAT_MESSAGE.ChatMessageType.SHOUT;
                    }

                    chatHistoryList.add(new ChatMessage(resultSet.getInt("user_id"), resultSet.getString("username"),
                            resultSet.getString("message"), chatMessageType,
                            page, resultSet.getLong("timestamp")));
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

    public static List<PlayerDetails> search(String type, String field, String input) {
        List<PlayerDetails> players = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();

            if (type.equals("contains")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " LIKE ?", sqlConnection);
                preparedStatement.setString(1, "%" + input + "%");
            } else if (type.equals("starts_with")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " LIKE ?", sqlConnection);
                preparedStatement.setString(1, input + "%");
            } else if (type.equals("ends_with")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " LIKE ?", sqlConnection);
                preparedStatement.setString(1, "%" + input);
            } else {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " = ?", sqlConnection);
                preparedStatement.setString(1, input);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PlayerDetails playerDetails = new PlayerDetails();
                PlayerDao.fill(playerDetails, resultSet);

                players.add(playerDetails);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return players;
    }

    public static List<Map<String, Object>> getChatlogsByUserId(int userId) {
        List<Map<String, Object>> chatLogs = new ArrayList<>();

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
                Map<String, Object> chatLog = new HashMap<>();
                chatLog.put("id", resultSet.getInt("id"));
                chatLog.put("userId", resultSet.getInt("userId")); // Usar el alias "userId"
                chatLog.put("username", resultSet.getString("username"));
                chatLog.put("message", resultSet.getString("message"));
                chatLog.put("roomName", resultSet.getString("room_name"));
                chatLog.put("roomId", resultSet.getInt("room_id"));
                chatLog.put("timestamp", resultSet.getLong("timestamp"));

                chatLogs.add(chatLog);
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

    public static List<Map<String, Object>> getMessengerMessagesByUserId(int userId) {
        List<Map<String, Object>> messengerMessages = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "SELECT 'Messenger Message' AS log_type, messenger_messages.id AS id, " +
                    "messenger_messages.sender_id AS userId, users.username AS username, " +
                    "messenger_messages.body AS message, messenger_messages.receiver_id AS friendId, " +
                    "friend_user.username AS friendName, " + // Agregamos esta línea
                    "messenger_messages.date AS timestamp FROM messenger_messages " +
                    "INNER JOIN users ON messenger_messages.sender_id = users.id " +
                    "LEFT JOIN users AS friend_user ON messenger_messages.receiver_id = friend_user.id " +
                    "WHERE messenger_messages.sender_id = ? " +
                    "LIMIT 100";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> messengerMessage = new HashMap<>();
                messengerMessage.put("logType", resultSet.getString("log_type"));
                messengerMessage.put("id", resultSet.getInt("id"));
                messengerMessage.put("userId", resultSet.getInt("userId"));
                messengerMessage.put("username", resultSet.getString("username"));
                messengerMessage.put("friendId", resultSet.getString("receiver_id"));
                messengerMessage.put("friendName", resultSet.getString("friendname"));
                messengerMessage.put("message", resultSet.getString("body"));
                messengerMessage.put("timestamp", resultSet.getLong("date"));

                messengerMessages.add(messengerMessage);
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
}
