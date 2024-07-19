package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class HousekeepingLogsDao {
    public static void logHousekeepingAction(String type, int userId, String username, String description, String userIp) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO housekeeping_staff_logs (type, user_id, username, description, user_ip, timestamp) VALUES (?, ?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, userIp);

            Date fechaActual = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String fechaFormateada = sdf.format(fechaActual);

            preparedStatement.setString(6, fechaFormateada);

            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<Map<String, Object>> getHousekeepingLogs(String type, int page) {
        List<Map<String, Object>> StaffActionLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM housekeeping_staff_logs WHERE type = ? ORDER BY id DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setString(1, type);
                preparedStatement.setInt(2, rows);
                preparedStatement.setInt(3, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> staffActionLog = new HashMap<>();
                    staffActionLog.put("id", resultSet.getInt("id"));
                    staffActionLog.put("userId", resultSet.getString("user_id"));
                    staffActionLog.put("userName", resultSet.getString("username"));
                    staffActionLog.put("description", resultSet.getString("description"));
                    staffActionLog.put("userIp", resultSet.getString("user_ip"));
                    staffActionLog.put("date", resultSet.getString("timestamp"));

                    StaffActionLogsList.add(staffActionLog);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return StaffActionLogsList;
    }

    public static List<Map<String, Object>> getAllLoginLogs(int page) {
        List<Map<String, Object>> LoginLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM housekeeping_login_log ORDER BY id DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> loginLog = new HashMap<>();
                    loginLog.put("id", resultSet.getInt("id"));
                    loginLog.put("userId", resultSet.getString("user_id"));
                    loginLog.put("userName", resultSet.getString("username"));
                    loginLog.put("userIp", resultSet.getString("ip_address"));
                    loginLog.put("date", resultSet.getString("login_time"));

                    LoginLogsList.add(loginLog);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return LoginLogsList;
    }
}
