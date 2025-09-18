package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingRCONLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingRCONCommandsDao {
    public static List<HousekeepingRCONLog> getAllTypeRCONLogs(int page, String sortBy, String type) {
        List<HousekeepingRCONLog> MassAlertsLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM housekeeping_rcon_logs WHERE type = ? ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setString(1, type);
                preparedStatement.setInt(2, rows);
                preparedStatement.setInt(3, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    MassAlertsLogsList.add(fill(resultSet));
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

    public static boolean insertRconLog(String type, String user, String moderator, String message) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            long unixTimestamp = System.currentTimeMillis() / 1000;

            java.sql.Date date = new java.sql.Date(unixTimestamp * 1000);

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

    private static HousekeepingRCONLog fill(ResultSet resultSet) throws Exception {
        return new HousekeepingRCONLog(
                resultSet.getInt("id"),
                resultSet.getString("type"),
                resultSet.getString("user"),
                resultSet.getString("moderator"),
                resultSet.getString("message"),
                resultSet.getString("timestamp")
        );
    }
}
