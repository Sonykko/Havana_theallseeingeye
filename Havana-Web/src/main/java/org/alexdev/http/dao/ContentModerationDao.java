package org.alexdev.http.dao;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class ContentModerationDao {

    public static void addReport(String type, int objectId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            long unixTimestamp = System.currentTimeMillis() / 1000;
            Date date = new Date(unixTimestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm d/MM/yyyy");
            String formattedDate = sdf.format(date);

            preparedStatement = Storage.getStorage().prepare("INSERT INTO cms_content_reports (type, object_id, message, timestamp) VALUES (?, ?, 'Has been reported the item " + type + " with the ID: " + objectId + ".', ?)", sqlConnection);
            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, objectId);
            preparedStatement.setString(3, formattedDate);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
