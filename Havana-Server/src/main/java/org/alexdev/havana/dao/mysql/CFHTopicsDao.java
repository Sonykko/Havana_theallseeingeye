package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.game.moderation.cfh.topics.CFHTopics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CFHTopicsDao {
    public static List<CFHTopics> getCFHTopics() {
        List<CFHTopics> CFHTopicsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM bans_reasons", sqlConnection);
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

    private static CFHTopics fill(ResultSet resultSet) throws Exception {
        return new CFHTopics(
                resultSet.getInt("id"),
                resultSet.getString("sanctionReasonId"),
                resultSet.getString("sanctionReasonValue"),
                resultSet.getString("sanctionReasonDesc")
        );
    }
}
