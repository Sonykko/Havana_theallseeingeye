package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingLogsDao {
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
