package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.moderation.ChatMessage;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.RoomData;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingRoomDao {
    public static List<Map<String, Object>> searchRoom(String type, String field, String input) {
        List<Map<String, Object>> RoomAdminList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            if (type.equals("ownerName")) {
                // Buscar el ID del usuario en la tabla users
                preparedStatement = Storage.getStorage().prepare("SELECT id FROM users WHERE username = ?", sqlConnection);
                preparedStatement.setString(1, input);

                resultSet = preparedStatement.executeQuery();

                // Verificar si se encontró un usuario con el nombre dado
                if (resultSet.next()) {
                    // Obtener el ID del usuario encontrado
                    String userId = resultSet.getString("id");

                    // Buscar en la tabla rooms usando el ID del usuario
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE owner_id = ?", sqlConnection);
                    preparedStatement.setString(1, userId);
                    resultSet = preparedStatement.executeQuery();
                } else {
                    // Si no se encontró el usuario, no hay necesidad de seguir buscando en rooms
                    resultSet.close();
                }
            } else {
                sqlConnection = Storage.getStorage().getConnection();

                if (type.equals("contains")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " LIKE ?", sqlConnection);
                    preparedStatement.setString(1, "%" + input + "%");
                } else if (type.equals("starts_with")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " LIKE ?", sqlConnection);
                    preparedStatement.setString(1, input + "%");
                } else if (type.equals("ends_with")) {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " LIKE ?", sqlConnection);
                    preparedStatement.setString(1, "%" + input);
                } else {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " = ?", sqlConnection);
                    preparedStatement.setString(1, input);
                }

                resultSet = preparedStatement.executeQuery();
            }
            while (resultSet.next()) {
                Map<String, Object> roomAdmin = new HashMap<>();
                roomAdmin.put("roomId", resultSet.getInt("id"));
                roomAdmin.put("ownerId", resultSet.getString("owner_id"));
                roomAdmin.put("name", resultSet.getString("name"));
                roomAdmin.put("description", resultSet.getString("description"));

                String ownerId = resultSet.getString("owner_id");
                String ownerName = getOwnerName(sqlConnection, ownerId);
                roomAdmin.put("ownerName", ownerName);

                RoomAdminList.add(roomAdmin);

            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return RoomAdminList;
    }

    private static String getOwnerName(Connection connection, String ownerId) throws SQLException {
        PreparedStatement userStatement = null;
        ResultSet userResultSet = null;

        try {
            // Consulta para obtener el nombre del dueño
            userStatement = Storage.getStorage().prepare("SELECT username FROM users WHERE id = ?", connection);
            userStatement.setString(1, ownerId);
            userResultSet = userStatement.executeQuery();

            if (userResultSet.next()) {
                return userResultSet.getString("username");
            }
        } finally {
            Storage.closeSilently(userResultSet);
            Storage.closeSilently(userStatement);
        }

        return null; // Retornar null si no se encuentra el nombre del dueño
    }

    public static List<Map<String, Object>> getRoom(String roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> roomAdminDataList = new ArrayList<>();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, roomId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Map<String, Object> roomAdminData = new HashMap<>();
                roomAdminData.put("category", resultSet.getInt("category"));
                roomAdminData.put("id", resultSet.getInt("id"));
                roomAdminData.put("ownerId", resultSet.getString("owner_id"));
                roomAdminData.put("name", resultSet.getString("name"));
                roomAdminData.put("description", resultSet.getString("description"));
                roomAdminData.put("accesstype", resultSet.getInt("accesstype"));

                roomAdminDataList.add(roomAdminData);
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return roomAdminDataList;
    }

    public static void updateRoom(int roomId, int category, String name, String description, int accesstype) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = sqlConnection.prepareStatement("UPDATE rooms SET category = ?, name = ?, description = ?, accesstype = ? WHERE id = ?");
            preparedStatement.setInt(1, category);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, accesstype);
            preparedStatement.setInt(5, roomId);

            int rowsUpdated = preparedStatement.executeUpdate();

            // Comprueba si se actualizó al menos una fila
            if (rowsUpdated > 0) {
                // Éxito: El registro se marcó como "Picked Up"
            } else {
                // Error: El registro no se actualizó, maneja el error apropiadamente
            }
        } catch (Exception e) {
            // Maneja los errores adecuadamente
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<Map<String, Object>> getModChatlog(int page, String sortBy) {
        List<Map<String, Object>> chatHistoryList = new ArrayList<>();

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
                    Map<String, Object> chatLog = new HashMap<>();
                    chatLog.put("id", resultSet.getInt("id"));
                    chatLog.put("userId", resultSet.getString("user_id"));
                    chatLog.put("username", resultSet.getString("username"));
                    chatLog.put("message", resultSet.getString("message"));
                    chatLog.put("roomName", resultSet.getString("room_name"));
                    chatLog.put("roomId", resultSet.getInt("room_id"));
                    chatLog.put("timestamp", resultSet.getLong("timestamp"));

                    chatHistoryList.add(chatLog);
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
                Map<String, Object> chatLog = new HashMap<>();
                chatLog.put("id", resultSet.getInt("id"));
                chatLog.put("userId", resultSet.getString("user_id"));
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

    public static List<Map<String, Object>> getConversationsBetweenUsers(int userId1, int userId2) {
        List<Map<String, Object>> conversations = new ArrayList<>();

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
                Map<String, Object> conversation = new HashMap<>();
                conversation.put("logType", resultSet.getString("log_type"));
                conversation.put("id", resultSet.getInt("id"));
                conversation.put("userId", resultSet.getInt("userId"));
                conversation.put("username", resultSet.getString("username"));
                conversation.put("friendId", resultSet.getString("friendId"));
                conversation.put("friendName", resultSet.getString("friendName"));
                conversation.put("message", resultSet.getString("message"));
                conversation.put("timestamp", resultSet.getLong("timestamp"));

                conversations.add(conversation);
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
}
