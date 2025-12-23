package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingBotGuide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingBotGuideDao {
    public static List<HousekeepingBotGuide> getAllSpeech(int page) {
        List<HousekeepingBotGuide> BotGuideSpeech = new ArrayList<>();


        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_botguide_speech LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    BotGuideSpeech.add(fill(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return BotGuideSpeech;
    }

    public static HousekeepingBotGuide getSpeechBySpeechKey(String speechKey) {
        HousekeepingBotGuide speech = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_botguide_speech WHERE speech_key = ?", sqlConnection);
            preparedStatement.setString(1, speechKey);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                speech = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return speech;
    }

    public static void create(String speechKey, String response, String speechTrigger) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO rooms_botguide_speech (speech_key, response, speech_trigger) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, speechKey);
            preparedStatement.setString(2, response);
            preparedStatement.setString(3, speechTrigger);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<HousekeepingBotGuide> search(String query) {
        List<HousekeepingBotGuide> speech = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_botguide_speech WHERE speech_key LIKE ? OR response LIKE ? OR speech_key LIKE ? LIMIT 20", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setString(2, query + "%");
            preparedStatement.setString(3, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                speech.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return speech;
    }

    public static void save(HousekeepingBotGuide botGuideSpeech, String speechKeyOriginal) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE rooms_botguide_speech SET speech_key = ?, response = ?, speech_trigger = ? WHERE speech_key = ?", sqlConnection);
            preparedStatement.setString(1, botGuideSpeech.getSpeechKey());
            preparedStatement.setString(2, botGuideSpeech.getResponse());
            preparedStatement.setString(3, botGuideSpeech.getSpeechTrigger());
            preparedStatement.setString(4, speechKeyOriginal);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void delete(String speechKey) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM rooms_botguide_speech WHERE speech_key = ?", sqlConnection);
            preparedStatement.setString(1, speechKey);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingBotGuide fill(ResultSet resultSet) throws Exception {
        return new HousekeepingBotGuide(
                resultSet.getString("speech_key"),
                resultSet.getString("response"),
                resultSet.getString("speech_trigger")
        );
    }
}
