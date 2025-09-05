package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingLog;
import org.alexdev.http.game.housekeeping.HousekeepingLoginLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static List<HousekeepingLog> getHousekeepingLogs(String type, int page) {
        List<HousekeepingLog> StaffActionLogsList = new ArrayList<>();

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
                    StaffActionLogsList.add(fillHousekeepingLog(resultSet));
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

    public static List<HousekeepingLoginLog> getAllLoginLogs(int page) {
        List<HousekeepingLoginLog> LoginLogsList = new ArrayList<>();

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
                    LoginLogsList.add(fillLoginLog(resultSet));
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

    private static HousekeepingLog fillHousekeepingLog(ResultSet resultSet) throws Exception {
        return new HousekeepingLog(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("description"),
                resultSet.getString("user_ip"),
                resultSet.getString("timestamp"),
                resultSet.getString("type")
        );
    }

    private static HousekeepingLoginLog fillLoginLog(ResultSet resultSet) throws Exception {
        return new HousekeepingLoginLog(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("ip_address"),
                resultSet.getString("login_time")
        );
    }
}
