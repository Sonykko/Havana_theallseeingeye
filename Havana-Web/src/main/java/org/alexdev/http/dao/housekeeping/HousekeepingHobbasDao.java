package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingHobbasDao {
    public static List<Map<String, Object>> getLogs(int page) {
        List<Map<String, Object>> HobbaFormsList = new ArrayList<>();

        int rows = 50;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                String query = "SELECT * FROM hobbas_forms " +
                        "ORDER BY id DESC LIMIT ? OFFSET ?";

                preparedStatement = Storage.getStorage().prepare(query, sqlConnection);

                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> HobbaForm = new HashMap<>();
                    HobbaForm.put("id", resultSet.getInt("id"));
                    HobbaForm.put("habboname", resultSet.getString("habboname"));
                    HobbaForm.put("email", resultSet.getString("email"));
                    HobbaForm.put("firstname", resultSet.getString("firstname"));
                    HobbaForm.put("lastname", resultSet.getString("lastname"));
                    HobbaForm.put("picked_up", resultSet.getString("picked_up"));
                    HobbaForm.put("timestamp", resultSet.getString("timestamp"));

                    HobbaFormsList.add(HobbaForm);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }
        return HobbaFormsList;
    }

    public static Map<String, String> checkHabboDetails(int userId) {
        Map<String, String> results = new HashMap<>();
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            // Obtener el username basado en el userID
            String queryUsername = "SELECT username FROM users WHERE id = ?";
            preparedStatement = Storage.getStorage().prepare(queryUsername, sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            String username = null;
            if (resultSet.next()) {
                username = resultSet.getString("username");
            } else {
                results.put("UserNotFound_RED", "<text class=\"alert alert-danger\">Can't find the user.</text>");
                return results; // No se encontró el usuario
            }

            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);

            // Comprobar si el usuario ya es Hobba
            String queryRank = "SELECT rank, created_at FROM users WHERE id = ?";
            preparedStatement = Storage.getStorage().prepare(queryRank, sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int rank = resultSet.getInt("rank");
                if (rank >= 3) {
                    results.put("AlreadyHobba_RED", "<text class=\"alert alert-danger\">User is already a Hobba.</text>");
                } else {
                    results.put("AlreadyHobba", "User is not already a Hobba.");
                }
                // Comprobar antigüedad
                long registeredAt = resultSet.getTimestamp("created_at").getTime();
                long now = System.currentTimeMillis();
                long oneMonth = 30L * 24 * 60 * 60 * 1000;

                if ((now - registeredAt) < oneMonth) {
                    results.put("RecentRegistration_RED", "<text class=\"alert alert-danger\">The Habbo character is not old enough. <i>(user: " + registeredAt + " / limit: 28)</i></text>");
                } else {
                    results.put("RecentRegistration", "The Habbo character is old enough. <i>(user: " + registeredAt + " / limit: 28)</i>");
                }
            } else {
                results.put("UserNotFound_RED", "<text class=\"alert alert-danger\">Can't find the user.</text>");
                return results; // No se encontró el usuario
            }

            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);

            // Buscar en hobbas_forms para comprobar si hay una aplicación
            String queryApplication = "SELECT COUNT(*) AS count FROM hobbas_forms WHERE habboname = ? AND picked_up = 0";
            preparedStatement = Storage.getStorage().prepare(queryApplication, sqlConnection);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt("count") > 0) {
                results.put("HaveApplication_RED", "<text class=\"alert alert-danger\">There is already an open application for this user.</text>");
            } else {
                results.put("HaveApplication", "There is not an open application for this user.");
            }

            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);

            // Buscar en users_bans por el userID (banned_value)
            String queryBans = "SELECT COUNT(*) AS count FROM users_bans WHERE banned_value = ?";
            preparedStatement = Storage.getStorage().prepare(queryBans, sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt("count") > 3) {
                results.put("TooManyBans_RED", "<text class=\"alert alert-danger\">User has been banned too many times. <i>(user: " + resultSet.getInt("count") + " / limit: 3)</i></text>");
            } else {
                results.put("TooManyBans", "User has not been banned too many times. <i>(user: " + resultSet.getInt("count") + " / limit: 3)</i>");
            }

            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);

            // Realizar las demás comprobaciones necesarias
            // Comprobar cantidad de salas
            String queryRooms = "SELECT COUNT(*) AS count FROM rooms WHERE owner_id = ?";
            preparedStatement = Storage.getStorage().prepare(queryRooms, sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt("count") < 2) {
                results.put("NotEnoughRooms_RED", "<text class=\"alert alert-danger\">User has not enough rooms. <i>(user: " + resultSet.getInt("count") + " / limit: 2)</i></text>");
            } else {
                results.put("NotEnoughRooms", "User has enough rooms. <i>(user: " + resultSet.getInt("count") + " / limit: 2)</i>");
            }

            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);

            // Comprobar cantidad de amigos
            String queryBuddies = "SELECT COUNT(*) AS count FROM messenger_friends WHERE to_id = ?";
            preparedStatement = Storage.getStorage().prepare(queryBuddies, sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt("count") < 2) {
                results.put("NotEnoughBuddies_RED", "<text class=\"alert alert-danger\">User has not enough buddies on his/her buddy list. <i>(user: " + resultSet.getInt("count") + " / limit: 2)</i></text>");
            } else {
                results.put("NotEnoughBuddies", "User has enough buddies on his/her buddy list. <i>(user: " + resultSet.getInt("count") + " / limit: 2)</i>");
            }

            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);

            // Comprobar la antigüedad del personaje
            String queryCharacterAge = "SELECT TIMESTAMPDIFF(DAY, created_at, NOW()) AS age FROM users WHERE id = ?";
            preparedStatement = Storage.getStorage().prepare(queryCharacterAge, sqlConnection);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt("age") < 18) {
                results.put("CharacterTooYoung_RED", "<text class=\"alert alert-danger\">User is not old enough to become a Hobba. <i>(user: " + resultSet.getInt("age") + " / limit: 18)</i></text>");
            } else {
                results.put("CharacterTooYoung", "User is old enough to become a Hobba. <i>(user: " + resultSet.getInt("age") + " / limit: 18)</i>");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return results;
    }

    public static void updateApplication(int logId) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE hobbas_forms SET picked_up = 1 WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, logId);

            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}