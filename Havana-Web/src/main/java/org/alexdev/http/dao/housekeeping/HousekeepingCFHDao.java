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
                    cfhLog.put("userId", resultSet.getInt("user_id")); // Cambiar a la columna correspondiente
                    cfhLog.put("username", resultSet.getString("user")); // Cambiar a la columna correspondiente
                    cfhLog.put("reason", resultSet.getString("reason")); // Cambiar a la columna correspondiente
                    cfhLog.put("roomName", resultSet.getString("room")); // Cambiar a la columna correspondiente
                    cfhLog.put("roomId", resultSet.getInt("room_id")); // Cambiar a la columna correspondiente
                    cfhLog.put("createdTime", resultSet.getString("created_time")); // Cambiar a la columna correspondiente
                    cfhLog.put("status", resultSet.getString("is_deleted")); // Cambiar a la columna correspondiente
                    cfhLog.put("cryId", resultSet.getString("cry_id")); // Cambiar a la columna correspondiente
                    cfhLog.put("moderator", resultSet.getString("moderator")); // Cambiar a la columna correspondiente
                    cfhLog.put("pickedTime", resultSet.getString("picked_time")); // Cambiar a la columna correspondiente
                    cfhLog.put("cfhId", resultSet.getInt("cfh_id")); // Cambiar a la columna correspondiente
                    cfhLog.put("action", resultSet.getString("action")); // Cambiar a la columna correspondiente
                    cfhLog.put("messageToUser", resultSet.getString("message_to_user")); // Cambiar a la columna correspondiente

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

    public static List<Map<String, Object>> getCFHlogAction(String cryIdSaved) {
        List<Map<String, Object>> CFHActionList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM cfh_logs WHERE cry_id = ?",
                    sqlConnection
            );
            preparedStatement.setString(1, cryIdSaved);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> cfhLog = new HashMap<>();
                cfhLog.put("userId", resultSet.getInt("user_id")); // Cambiar a la columna correspondiente
                cfhLog.put("username", resultSet.getString("user")); // Cambiar a la columna correspondiente
                cfhLog.put("reason", resultSet.getString("reason")); // Cambiar a la columna correspondiente
                cfhLog.put("roomName", resultSet.getString("room")); // Cambiar a la columna correspondiente
                cfhLog.put("roomId", resultSet.getInt("room_id")); // Cambiar a la columna correspondiente
                cfhLog.put("createdTime", resultSet.getString("created_time")); // Cambiar a la columna correspondiente
                cfhLog.put("status", resultSet.getString("is_deleted")); // Cambiar a la columna correspondiente
                cfhLog.put("cryId", resultSet.getString("cry_id")); // Cambiar a la columna correspondiente
                cfhLog.put("moderator", resultSet.getString("moderator")); // Cambiar a la columna correspondiente
                cfhLog.put("pickedTime", resultSet.getString("picked_time")); // Cambiar a la columna correspondiente
                cfhLog.put("cfhId", resultSet.getInt("cfh_id")); // Cambiar a la columna correspondiente
                cfhLog.put("action", resultSet.getString("action")); // Cambiar a la columna correspondiente
                cfhLog.put("messageToUser", resultSet.getString("message_to_user")); // Cambiar a la columna correspondiente

                CFHActionList.add(cfhLog);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return CFHActionList;
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
}