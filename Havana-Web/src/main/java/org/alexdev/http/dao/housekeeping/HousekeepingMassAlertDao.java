package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingMassAlertLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingMassAlertDao {
    public static List<HousekeepingMassAlertLog> MassAlertsLogs(int page, String sortBy) {
        List<HousekeepingMassAlertLog> MassAlertsLogsList = new ArrayList<>();

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

    private static HousekeepingMassAlertLog fill(ResultSet resultSet) throws Exception {
        return new HousekeepingMassAlertLog(
                resultSet.getInt("id"),
                resultSet.getString("type"),
                resultSet.getString("user"),
                resultSet.getString("moderator"),
                resultSet.getString("message"),
                resultSet.getString("timestamp")
        );
    }
}
