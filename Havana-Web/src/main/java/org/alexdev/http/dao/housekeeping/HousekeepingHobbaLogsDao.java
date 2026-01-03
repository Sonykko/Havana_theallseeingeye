package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingHobbaLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingHobbaLogsDao {
    public static List<HousekeepingHobbaLog> getHobbaLogs(int page) {
        List<HousekeepingHobbaLog> HobbaLogList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM housekeeping_audit_log ORDER BY created_at DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    HobbaLogList.add(fill(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }

        }

        return HobbaLogList;
    }

    private static HousekeepingHobbaLog fill(ResultSet resultSet) throws Exception {
        return new HousekeepingHobbaLog(
                resultSet.getString("action"),
                resultSet.getInt("user_id"),
                resultSet.getInt("target_id"),
                resultSet.getString("message"),
                resultSet.getString("extra_notes"),
                resultSet.getTimestamp("created_at").getTime() / 1000
        );
    }
}
