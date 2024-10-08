package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingRCONCommandsDao {
    public static List<Map<String, Object>> getLogs(int page, String type) {
        List<Map<String, Object>> GiveBadgesList = new ArrayList<>();

        int rows = 50;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                String query = "SELECT * FROM housekeeping_commands_rcon WHERE type = ?" +
                        "ORDER BY id DESC LIMIT ? OFFSET ?";

                preparedStatement = Storage.getStorage().prepare(query, sqlConnection);

                preparedStatement.setString(1, type);
                preparedStatement.setInt(2, rows);
                preparedStatement.setInt(3, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> GiveBadges = new HashMap<>();
                    GiveBadges.put("id", resultSet.getInt("id"));
                    GiveBadges.put("user", resultSet.getString("user"));
                    GiveBadges.put("argument1", resultSet.getString("argument_1"));
                    GiveBadges.put("argument2", resultSet.getString("argument_2"));
                    GiveBadges.put("type", resultSet.getString("type"));
                    GiveBadges.put("timestamp", resultSet.getString("timestamp"));

                    GiveBadgesList.add(GiveBadges);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }
        return GiveBadgesList;
    }

    public static boolean insertLog(String user, String argument_1, String argument_2, String type) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();

            // Obtener la fecha y hora actual en formato Unix timestamp
            long unixTimestamp = System.currentTimeMillis() / 1000;

            // Crear un objeto Date a partir del timestamp
            Date date = new Date(unixTimestamp * 1000);

            // Formatear la fecha y hora en el formato deseado
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm d/MM/yyyy");
            String formattedDate = sdf.format(date);

            preparedStatement = sqlConnection.prepareStatement("INSERT INTO housekeeping_commands_rcon (user, argument_1, argument_2, type, timestamp) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, argument_1);
            preparedStatement.setString(3, argument_2);
            preparedStatement.setString(4, type);
            preparedStatement.setString(5, formattedDate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return false;
    }
}