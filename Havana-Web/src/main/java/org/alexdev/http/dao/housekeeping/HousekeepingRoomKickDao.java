package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingRCONLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingRoomKickDao {
    public static List<HousekeepingRCONLog> RemoteRoomKicksLogs(int page, String sortBy) {
        List<HousekeepingRCONLog> RemoteRoomKicksLogsList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM housekeeping_rcon_logs WHERE type = 'REMOTE_ROOM_KICK' OR type = 'REMOTE_ROOM_ALERT' ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    RemoteRoomKicksLogsList.add(fill(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return RemoteRoomKicksLogsList;
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
