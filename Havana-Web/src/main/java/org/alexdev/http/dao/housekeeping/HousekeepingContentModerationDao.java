package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Map<String, Object>> searchContentReports(int limit, String type) {
        List<Map<String, Object>> contentReportsList = new ArrayList<>();

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
                Map<String, Object> contentReport = new HashMap<>();

                contentReport.put("id", resultSet.getBigDecimal("id"));
                contentReport.put("type", resultSet.getString("type"));
                contentReport.put("objectId", resultSet.getInt("object_id"));
                contentReport.put("message", resultSet.getString("message"));
                contentReport.put("isModerated", resultSet.getInt("is_moderated"));
                contentReport.put("timestamp", resultSet.getString("timestamp"));

                contentReportsList.add(contentReport);
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
}
