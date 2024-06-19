package org.alexdev.http.dao;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;

public class HobbasDao {
    public static boolean insertApplyForm(String habboname, String email, String firstname, String lastname) {
        boolean insertSuccess = false;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String formattedTimestamp = dateFormat.format(new Date());

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = sqlConnection.prepareStatement(
                    "INSERT INTO hobbas_forms (habboname, email, firstname, lastname, picked_up, timestamp) VALUES (?, ?, ?, ?, 0, ?)");
            preparedStatement.setString(1, habboname);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, firstname);
            preparedStatement.setString(4, lastname);
            preparedStatement.setString(5, formattedTimestamp);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                insertSuccess = true;
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return insertSuccess;
    }
}
