package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingCFHTopics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingCFHTopicsDao {
    public static void create(String sanctionReasonId, String sanctionReasonValue, String sanctionReasonDesc) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO bans_reasons (sanctionReasonId, sanctionReasonValue, sanctionReasonDesc) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, sanctionReasonId);
            preparedStatement.setString(2, sanctionReasonValue);
            preparedStatement.setString(3, sanctionReasonDesc);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<HousekeepingCFHTopics> getCFHTopics() {
        List<HousekeepingCFHTopics> CFHTopicsList = new ArrayList<>();

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

    public static HousekeepingCFHTopics getCFHTopicById(int id) {
        HousekeepingCFHTopics CFHTopic = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM bans_reasons WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CFHTopic = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return CFHTopic;
    }


    public static List<HousekeepingCFHTopics> searchTopics(String query) {
        List<HousekeepingCFHTopics> topics = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM bans_reasons WHERE sanctionReasonId LIKE ? OR sanctionReasonValue LIKE ? OR sanctionReasonDesc LIKE ? OR id = ? LIMIT 50", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setString(2, query + "%");
            preparedStatement.setString(3, query + "%");
            preparedStatement.setString(4, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                topics.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return topics;
    }

    public static void save(HousekeepingCFHTopics topic) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE bans_reasons SET sanctionReasonId = ?, sanctionReasonValue = ?, sanctionReasonDesc = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, topic.getSanctionReasonId());
            preparedStatement.setString(2, topic.getSanctionReasonValue());
            preparedStatement.setString(3, topic.getSanctionReasonDesc());
            preparedStatement.setInt(4, topic.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void delete(int topicId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM bans_reasons WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, topicId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
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
