package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingPlayerDao {

    public static List<PlayerDetails> getPlayers(int page, boolean zeroCoinsFlag, String sortBy) {
        List<PlayerDetails> players = new ArrayList<>();

        int rows = 25;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                String statement = "";

                if (zeroCoinsFlag) {
                    statement += " AND credits = 0 ";
                }

                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE username <> '' " + statement + "ORDER BY " + sortBy + " DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

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
        }

        return players;
    }

    public static List<PlayerDetails> search(String type, String field, String input) {
        List<PlayerDetails> players = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();

            if (type.equals("contains")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " LIKE ?", sqlConnection);
                preparedStatement.setString(1, "%" + input + "%");
            } else if (type.equals("starts_with")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " LIKE ?", sqlConnection);
                preparedStatement.setString(1, input + "%");
            } else if (type.equals("ends_with")) {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " LIKE ?", sqlConnection);
                preparedStatement.setString(1, "%" + input);
            } else {
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE " + field + " = ?", sqlConnection);
                preparedStatement.setString(1, input);
            }

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

    public static void logLogin(int userId, String username, String ipAddress) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = sqlConnection.prepareStatement("INSERT INTO housekeeping_login_log (user_id, username, login_time, ip_address) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String formattedTimestamp = dateFormat.format(new Timestamp(System.currentTimeMillis()));

            preparedStatement.setString(3, formattedTimestamp);
            preparedStatement.setString(4, ipAddress);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void unbanUser(int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = sqlConnection.prepareStatement("UPDATE users_bans SET is_active = 0 WHERE banned_value = CONVERT(?, CHAR)");
            preparedStatement.setInt(1, userId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Éxito: El registro se marcó como "Picked Up"
            } else {
                // Error: El registro no se actualizó, maneja el error apropiadamente
            }
        } catch (Exception e) {
            // Maneja los errores adecuadamente
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void setRank(String username, int rankId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE users SET rank = ? WHERE username = ?", sqlConnection);
            preparedStatement.setInt(1, rankId);
            preparedStatement.setString(2, username);
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void setRankTextVars(int rankId, String rankName, String rankBadge, String rankDescription) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {

            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE ranks SET name = ?, badge = ?, description = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, rankName);
            preparedStatement.setString(2, rankBadge);
            preparedStatement.setString(3, rankDescription);
            preparedStatement.setInt(4, rankId);
            preparedStatement.execute();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static String CheckDBName(String username) {
        String UsernameIsValid = "";

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE username = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UsernameIsValid = (resultSet.getString("username"));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return UsernameIsValid;
    }

    public static String CheckDBUserId(String userId) {
        String UserIdIsValid = "";

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM users WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UserIdIsValid = (resultSet.getString("id"));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return UserIdIsValid;
    }

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

    public static List<Map<String, Object>> getTrustedPersonLogs(int page) {
        List<Map<String, Object>> TrustedPersonList = new ArrayList<>();

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
                    Map<String, Object> TrustedPerson = new HashMap<>();
                    TrustedPerson.put("id", resultSet.getInt("id"));
                    TrustedPerson.put("userName", resultSet.getString("username"));
                    TrustedPerson.put("userId", resultSet.getInt("user_id"));
                    TrustedPerson.put("staff", resultSet.getString("staff"));
                    TrustedPerson.put("type", resultSet.getInt("type"));
                    TrustedPerson.put("timestamp", resultSet.getString("timestamp"));

                    TrustedPersonList.add(TrustedPerson);
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
}
