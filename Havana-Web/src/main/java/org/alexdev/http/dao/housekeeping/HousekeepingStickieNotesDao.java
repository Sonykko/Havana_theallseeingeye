package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingStickieNotesDao {
    public static List<Map<String, Object>> searchStickieNotes(int criteria, int limit, String stickieText) {
        List<Map<String, Object>> stickieNotesList = new ArrayList<>();

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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (resultSet.next()) {
                Map<String, Object> stickieNote = new HashMap<>();

                var playerDetails = PlayerDao.getDetails(resultSet.getInt("user_id"));

                String username = "";

                if (playerDetails != null) {
                    username = playerDetails.getName();
                }

                stickieNote.put("userName", username);

                String contents = resultSet.getString("custom_data");

                String newMessage = "";

                if (contents.length() > 6) {
                    newMessage = StringUtil.filterInput(contents.substring(6), false);
                }

                stickieNote.put("text", newMessage);

                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Timestamp updatedAt = resultSet.getTimestamp("updated_at");

                stickieNote.put("createdAt", createdAt != null ? dateFormat.format(createdAt) : null);
                stickieNote.put("updatedAt", updatedAt != null ? dateFormat.format(updatedAt) : null);

                stickieNote.put("id", resultSet.getBigDecimal("id"));
                stickieNote.put("roomId", resultSet.getInt("room_id"));

                stickieNotesList.add(stickieNote);
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
}
