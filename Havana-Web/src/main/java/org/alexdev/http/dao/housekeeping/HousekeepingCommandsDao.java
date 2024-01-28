package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.moderation.ChatMessage;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.RoomData;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingCommandsDao {
    public static boolean insertRconLog(String type, String user, String moderator, String message) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            // Obtener la fecha y hora actual en formato Unix timestamp
            long unixTimestamp = System.currentTimeMillis() / 1000;

            // Crear un objeto Date a partir del timestamp
            Date date = new Date(unixTimestamp * 1000);

            // Formatear la fecha y hora en el formato deseado
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm d/MM/yyyy");
            String formattedDate = sdf.format(date);

            preparedStatement = sqlConnection.prepareStatement("INSERT INTO housekeeping_rcon_logs (type, user, moderator, message, timestamp) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, user);
            preparedStatement.setString(3, moderator);
            preparedStatement.setString(4, message);
            preparedStatement.setString(5, formattedDate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return false;
    }

    public static List<Map<String, Object>> RemoteAlertLogs(int page, String sortBy) {
        List<Map<String, Object>> RemoteAlertLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare(
                        "SELECT * FROM housekeeping_rcon_logs WHERE type = 'REMOTE_ALERT' ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?",
                        sqlConnection
                );
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> remoteAlertLog = new HashMap<>();
                    remoteAlertLog.put("id", resultSet.getInt("id"));
                    remoteAlertLog.put("type", resultSet.getString("type"));
                    remoteAlertLog.put("user", resultSet.getString("user"));
                    remoteAlertLog.put("moderator", resultSet.getString("moderator"));
                    remoteAlertLog.put("message", resultSet.getString("message"));
                    remoteAlertLog.put("timestamp", resultSet.getString("timestamp"));

                    RemoteAlertLogsList.add(remoteAlertLog);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return RemoteAlertLogsList;
    }

    public static List<Map<String, Object>> RemoteKickLogs(int page, String sortBy) {
        List<Map<String, Object>> RemoteKickLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare(
                        "SELECT * FROM housekeeping_rcon_logs WHERE type = 'REMOTE_KICK' ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?",
                        sqlConnection
                );
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> remoteKickLog = new HashMap<>();
                    remoteKickLog.put("id", resultSet.getInt("id"));
                    remoteKickLog.put("type", resultSet.getString("type"));
                    remoteKickLog.put("user", resultSet.getString("user"));
                    remoteKickLog.put("moderator", resultSet.getString("moderator"));
                    remoteKickLog.put("message", resultSet.getString("message"));
                    remoteKickLog.put("timestamp", resultSet.getString("timestamp"));

                    RemoteKickLogsList.add(remoteKickLog);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return RemoteKickLogsList;
    }

    public static List<Map<String, Object>> MassAlertsLogs(int page, String sortBy) {
        List<Map<String, Object>> MassAlertsLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare(
                        "SELECT * FROM housekeeping_rcon_logs WHERE type = 'HOTEL_ALERT' ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?",
                        sqlConnection
                );
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> massAlertLog = new HashMap<>();
                    massAlertLog.put("id", resultSet.getInt("id"));
                    massAlertLog.put("type", resultSet.getString("type"));
                    massAlertLog.put("moderator", resultSet.getString("moderator"));
                    massAlertLog.put("message", resultSet.getString("message"));
                    massAlertLog.put("timestamp", resultSet.getString("timestamp"));

                    MassAlertsLogsList.add(massAlertLog);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return MassAlertsLogsList;
    }

    public static List<Map<String, Object>> getCFHTopics() {
        List<Map<String, Object>> CFHTopicsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "SELECT * FROM bans_reasons";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> CFHTopics = new HashMap<>();
                CFHTopics.put("id", resultSet.getInt("id"));
                CFHTopics.put("sanctionReasonId", resultSet.getString("sanctionReasonId"));
                CFHTopics.put("sanctionReasonValue", resultSet.getString("sanctionReasonValue"));
                CFHTopics.put("sanctionReasonDesc", resultSet.getString("sanctionReasonDesc"));

                CFHTopicsList.add(CFHTopics);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return CFHTopicsList;
    }
}
