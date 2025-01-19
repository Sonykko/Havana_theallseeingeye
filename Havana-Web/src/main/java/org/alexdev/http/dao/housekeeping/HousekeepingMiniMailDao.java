package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.dao.MinimailDao;
import org.alexdev.http.game.minimail.MinimailMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingMiniMailDao {
    public static List<MinimailMessage> getReportedMessages(int userId, boolean sender) {
        List<MinimailMessage> messages = new ArrayList<>();

        String criteria = "";

        if (sender) {
            criteria = "sender_id";
        } else {
            criteria = "to_id";
        }

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_minimail WHERE " + criteria + " = ? AND is_reported = 1 AND is_moderated = 0 LIMIT 25", sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int messageId = resultSet.getInt("id");
                int senderId = resultSet.getInt("sender_id");

                MinimailMessage messageDetails = MinimailDao.getMessage(messageId, senderId);

                if (messageDetails != null) {
                    messages.add(messageDetails);
                }
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return messages;
    }

    public static void archiveReportedMessage(int messageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_minimail SET is_moderated = 1 WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, messageId);
            preparedStatement.execute();
        } catch (Exception ex) {
            Storage.logError(ex);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}