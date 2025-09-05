package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingCFHTopics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingCFHTopicsDao {
    public static List<HousekeepingCFHTopics> getCFHTopics() {
        List<HousekeepingCFHTopics> CFHTopicsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            String query = "SELECT * FROM bans_reasons";

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CFHTopicsList.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return CFHTopicsList;
    }

    private static HousekeepingCFHTopics fill(ResultSet resultSet) throws Exception {
        return new HousekeepingCFHTopics(
                resultSet.getInt("id"),
                resultSet.getString("sanctionReasonId"),
                resultSet.getString("sanctionReasonValue"),
                resultSet.getString("sanctionReasonDesc")
        );
    }
}
