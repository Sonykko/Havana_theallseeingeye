package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.GroupDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.groups.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingGroupsDao {
    public static List<Group> getAllGroups(int page) {
        List<Group> groups = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM groups_details LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String groupAdmin = resultSet.getString("id");

                    Group group = GroupDao.fill(resultSet);

                    groups.add(group);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return groups;
    }

    public static List<Group> searchGroups(String query) {
        List<Group> groups = new ArrayList<>();

        int userId = PlayerDao.getId(query);

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM groups_details WHERE name LIKE ? OR owner_id = ? LIMIT 20", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setInt(2, userId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String groupAdmin = resultSet.getString("id");

                Group group = GroupDao.fill(resultSet);

                groups.add(group);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return groups;
    }
}