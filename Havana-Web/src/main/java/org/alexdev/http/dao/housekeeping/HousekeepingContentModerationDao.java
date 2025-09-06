package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingContentModeration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingContentModerationDao {

    public static int countReports(String reportType) {
        int count = 0;
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            preparedStatement = Storage.getStorage().prepare("SELECT COUNT(*) FROM cms_content_reports WHERE type = ? AND is_moderated = 0", sqlConnection);
            preparedStatement.setString(1, reportType);

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

    public static List<HousekeepingContentModeration> searchContentReports(int limit, String type) {
        List<HousekeepingContentModeration> contentReportsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_content_reports WHERE type = ? AND is_moderated = 0 ORDER BY id DESC LIMIT ?", sqlConnection);
            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, limit);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                contentReportsList.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
        return contentReportsList;
    }

    public static void setAsModerated(int reportId) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_content_reports SET is_moderated = 1 WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, reportId);

            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingContentModeration fill(ResultSet resultSet) throws Exception {
        return new HousekeepingContentModeration(
                resultSet.getInt("id"),
                resultSet.getString("type"),
                resultSet.getString("object_id"),
                resultSet.getString("message"),
                resultSet.getInt("is_moderated"),
                resultSet.getString("timestamp")
        );
    }
}
