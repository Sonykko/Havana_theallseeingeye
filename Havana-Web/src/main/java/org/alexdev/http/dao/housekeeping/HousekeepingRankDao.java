package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.game.housekeeping.HousekeepingRankVar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingRankDao {
    public static List<HousekeepingRankVar> getAllRanksVars() {
        List<HousekeepingRankVar> allRanksList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM ranks ORDER BY id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allRanksList.add(fillVars(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return allRanksList;
    }

    public static HousekeepingRankVar getRankVarByRankId(int rankId) {
        HousekeepingRankVar rankVar = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM ranks WHERE id = ? ORDER BY id ASC", sqlConnection);
            preparedStatement.setInt(1, rankId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                rankVar = fillVars(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return rankVar;
    }

    public static List<PlayerDetails> getAllStaffsNames() {
        List<PlayerDetails> allStaffsNamesList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM users WHERE rank > 2 ORDER BY rank DESC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int staffId = resultSet.getInt("id");
                PlayerDetails staffDetails = PlayerDao.getDetails(staffId);
                allStaffsNamesList.add(staffDetails);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return allStaffsNamesList;
    }

    private static HousekeepingRankVar fillVars(ResultSet resultSet) throws Exception {
        return new HousekeepingRankVar(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("badge"),
                resultSet.getString("description")
        );
    }
}
