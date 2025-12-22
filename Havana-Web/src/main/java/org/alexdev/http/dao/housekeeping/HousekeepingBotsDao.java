package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingBotsDao {
    public static HousekeepingBot getBotByBotId(int botId) {
        HousekeepingBot botData = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_bots WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, botId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                botData = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
            Storage.closeSilently(resultSet);
        }

        return botData;
    }

    public static List<HousekeepingBot> getAllBots() {
        List<HousekeepingBot> botData = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_bots", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                botData.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
            Storage.closeSilently(resultSet);
        }

        return botData;
    }

    public static List<HousekeepingBot> search(String query) {
        List<HousekeepingBot> bots = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_bots WHERE name LIKE ? OR id = ? LIMIT 20", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setString(2, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bots.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return bots;
    }

    public static void save(HousekeepingBot bot) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE rooms_bots SET name = ?, mission = ?, figure = ?, figure_flash = ?, speech = ?, response = ?, unrecognised_response = ?, hand_items = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, bot.getName());
            preparedStatement.setString(2, bot.getMission());
            preparedStatement.setString(3, bot.getFigure());
            preparedStatement.setString(4, bot.getFigureFlash());
            preparedStatement.setString(5, bot.getSpeeches());
            preparedStatement.setString(6, bot.getResponses());
            preparedStatement.setString(7, bot.getUnrecognisedSpeech());
            preparedStatement.setString(8, bot.getDrinks());
            preparedStatement.setInt(9, bot.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingBot fill(ResultSet resultSet) throws Exception {
        String[] look = resultSet.getString("start_look").split(",");

        return new HousekeepingBot(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("mission"),
                resultSet.getInt("x"),
                resultSet.getInt("y"),
                Integer.parseInt(look[0]),
                Integer.parseInt(look[1]),
                resultSet.getString("figure"),
                resultSet.getString("figure_flash"),
                resultSet.getInt("room_id"),
                resultSet.getString("walkspace"),
                resultSet.getString("speech"),
                resultSet.getString("response"),
                resultSet.getString("unrecognised_response"),
                resultSet.getString("hand_items")
        );
    }
}
