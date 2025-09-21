package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.moderation.ChatMessage;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.http.game.housekeeping.HousekeepingRoomChatLog;
import org.alexdev.http.game.housekeeping.HousekeepingWordfilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.alexdev.havana.dao.mysql.RoomDao.getRoomById;

public class HousekeepingRoomDao {
    public static List<Map<String, Object>> searchRoom1(String type, String field, String input) {
        List<Map<String, Object>> rooms = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            if (type.equals("contains")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " LIKE ? AND owner_id > 0 LIMIT 100", sqlConnection);
                preparedStatement.setString(1, "%" + input + "%");
            } else if (type.equals("starts_with")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " LIKE ? AND owner_id > 0 LIMIT 100", sqlConnection);
                preparedStatement.setString(1, input + "%");
            } else if (type.equals("ends_with")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " LIKE ? AND owner_id > 0 LIMIT 100", sqlConnection);
                preparedStatement.setString(1, "%" + input);
            } else if (type.equals("ownerName")) {
                preparedStatement = Storage.getStorage().prepare("SELECT r.*, u.id as userId " +
                        "FROM rooms r " +
                        "INNER JOIN users u ON r.owner_id = u.id " +
                        "WHERE u.username = ? AND r.owner_id > 0 LIMIT 100", sqlConnection);
                preparedStatement.setString(1, input);
            } else {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE " + field + " = ? AND owner_id > 0 LIMIT 100", sqlConnection);
                preparedStatement.setString(1, input);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int roomSearch = resultSet.getInt("id");

                Room room = getRoomById(roomSearch);

                Map<String, Object> roomAdmin = new HashMap<>();
                roomAdmin.put("roomId", room.getId());
                roomAdmin.put("ownerId", room.getData().getOwnerId());
                roomAdmin.put("ownerName", room.getData().getOwnerName());
                roomAdmin.put("name", room.getData().getName());
                roomAdmin.put("description", room.getData().getDescription());
                roomAdmin.put("status", room.getData().getAccessTypeId());

                String categoryId = resultSet.getString("category");
                String categoryName = getCategoryName1(sqlConnection, categoryId);
                roomAdmin.put("categoryName", categoryName);

                rooms.add(roomAdmin);

            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rooms;
    }

    private static String getCategoryName1(Connection connection, String categoryId) throws SQLException {
        PreparedStatement userStatement = null;
        ResultSet userResultSet = null;

        try {
            userStatement = Storage.getStorage().prepare("SELECT name FROM rooms_categories WHERE id = ?", connection);
            userStatement.setString(1, categoryId);
            userResultSet = userStatement.executeQuery();

            if (userResultSet.next()) {
                return userResultSet.getString("name");
            }
        } finally {
            Storage.closeSilently(userResultSet);
            Storage.closeSilently(userStatement);
        }

        return null;
    }

    public static List<Room> getRoom1(int roomAdminId) {
        List<Room> rooms = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, roomAdminId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String roomAdmin = resultSet.getString("id");

                Room room = getRoomById(Integer.parseInt(roomAdmin));

                rooms.add(room);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rooms;
    }

    public static List<Map<String, Object>> getAllPrivateRoomsCat() {
        List<Map<String, Object>> roomCats = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "SELECT * FROM rooms_categories WHERE public_spaces = 0 AND isnode = 0";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> roomCat = new HashMap<>();
                roomCat.put("id", resultSet.getInt("id"));
                roomCat.put("categoryName", resultSet.getString("name"));

                roomCats.add(roomCat);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return roomCats;
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

    public static int getClonedRoom(int ownerId) {
        int roomId = 0;
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms WHERE owner_id = ? ORDER BY id DESC LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, ownerId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                roomId = resultSet.getInt("id");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return roomId + 1;
    }
}
