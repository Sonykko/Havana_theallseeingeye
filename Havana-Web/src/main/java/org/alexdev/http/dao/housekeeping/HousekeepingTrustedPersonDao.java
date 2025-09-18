package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.game.housekeeping.HousekeepingTrustedPersonLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingTrustedPersonDao {
    public static void setTrustedPerson(String username, int userID, String type) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            if (username != null && !username.isEmpty()) {
                preparedStatement = Storage.getStorage().prepare("UPDATE users SET trusted_person = ? WHERE username = ?", sqlConnection);
                preparedStatement.setString(1, type);
                preparedStatement.setString(2, username);
            } else if (userID > 0) {
                preparedStatement = Storage.getStorage().prepare("UPDATE users SET trusted_person = ? WHERE id = ?", sqlConnection);
                preparedStatement.setString(1, type);
                preparedStatement.setInt(2, userID);
            }
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void logTrustedPerson(String username, int userID, String staff, String type) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = sqlConnection.prepareStatement("INSERT INTO housekeeping_trusted_person (username, user_id, staff, type, timestamp) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, userID);
            preparedStatement.setString(3, staff);
            preparedStatement.setString(4, type);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String formattedTimestamp = dateFormat.format(new Timestamp(System.currentTimeMillis()));

            preparedStatement.setString(5, formattedTimestamp);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<HousekeepingTrustedPersonLog> getTrustedPersonLogs(int page) {
        List<HousekeepingTrustedPersonLog> TrustedPersonList = new ArrayList<>();

        int rows = 25;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                String query = "SELECT * FROM housekeeping_trusted_person ORDER BY id DESC LIMIT ? OFFSET ?";

                preparedStatement = Storage.getStorage().prepare(query, sqlConnection);

                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    TrustedPersonList.add(fill(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }
        return TrustedPersonList;
    }

    public static List<PlayerDetails> getActiveTrustedPersons() {
        List<PlayerDetails> players = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE trusted_person = 1 ORDER BY username DESC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PlayerDetails playerDetails = new PlayerDetails();
                PlayerDao.fill(playerDetails, resultSet);

                players.add(playerDetails);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return players;
    }

    private static HousekeepingTrustedPersonLog fill(ResultSet resultSet) throws Exception {
        return new HousekeepingTrustedPersonLog(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getInt("user_id"),
                resultSet.getString("staff"),
                resultSet.getInt("type"),
                resultSet.getString("timestamp")
                );
    }
}
