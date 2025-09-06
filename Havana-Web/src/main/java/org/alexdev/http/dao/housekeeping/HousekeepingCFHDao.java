package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingCFH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingCFHDao {

    public static List<HousekeepingCFH> getCFHlog(int page, String sortBy) {
        List<HousekeepingCFH> CFHList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT cfh_logs.* FROM cfh_logs ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    CFHList.add(fill(resultSet));
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

    public static HousekeepingCFH getCFHLogByCryId(String cryIdSaved) {
        HousekeepingCFH cfh = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cfh_logs WHERE cry_id = ?", sqlConnection);
            preparedStatement.setString(1, cryIdSaved);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cfh = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return cfh;
    }
    private static HousekeepingCFH fill(ResultSet resultSet) throws Exception {
        return new HousekeepingCFH(
                resultSet.getInt("cfh_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("user"),
                resultSet.getString("reason"),
                resultSet.getString("room"),
                resultSet.getInt("room_id"),
                resultSet.getString("created_time"),
                resultSet.getBoolean("is_deleted"),
                resultSet.getString("cry_id"),
                resultSet.getString("moderator"),
                resultSet.getString("picked_time"),
                resultSet.getString("action"),
                resultSet.getString("message_to_user")
        );
    }
}