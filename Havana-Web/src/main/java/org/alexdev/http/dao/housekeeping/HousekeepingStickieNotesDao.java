package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.util.StringUtil;
import org.alexdev.http.game.housekeeping.HousekeepingStickieNote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingStickieNotesDao {
    public static List<HousekeepingStickieNote> searchStickieNotes(int criteria, int limit, String stickieText) {
        List<HousekeepingStickieNote> stickieNotesList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "";

            if (criteria >= 1) {
                query = "SELECT * FROM items WHERE definition_id = 244 AND user_id = ? AND room_id >= 1000 AND custom_data <> ? ORDER BY id DESC LIMIT ?";
                preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
                preparedStatement.setInt(1, criteria);
                preparedStatement.setString(2, stickieText);
                preparedStatement.setInt(3, limit);
            } else if (criteria == 0) {
                query = "SELECT * FROM items WHERE definition_id = 244 AND room_id >= 1000 AND custom_data <> ? ORDER BY id DESC LIMIT ?";
                preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
                preparedStatement.setString(1, stickieText);
                preparedStatement.setInt(2, limit);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                stickieNotesList.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
        return stickieNotesList;
    }

    public static int countStickieNotes(String stickieText) {
        int count = 0;
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            preparedStatement = Storage.getStorage().prepare("SELECT COUNT(*) FROM items WHERE definition_id = 244 AND room_id >= 1000 AND custom_data <> ?", sqlConnection);
            preparedStatement.setString(1, stickieText);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return count;
    }

    private static HousekeepingStickieNote fill(ResultSet resultSet) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        var playerDetails = PlayerDao.getDetails(resultSet.getInt("user_id"));
        String username = "";
        if (playerDetails != null) {
            username = playerDetails.getName();
        }

        String contents = resultSet.getString("custom_data");
        String newMessage = "";
        if (contents.length() > 6) {
            newMessage = StringUtil.filterInput(contents.substring(6), false);
        }

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");

        return new HousekeepingStickieNote(
                username,
                newMessage,
                createdAt != null ? dateFormat.format(createdAt) : null,
                updatedAt != null ? dateFormat.format(updatedAt) : null,
                resultSet.getBigDecimal("id"),
                resultSet.getInt("room_id")
        );
    }
}
