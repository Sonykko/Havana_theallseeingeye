package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.game.bot.BotGuide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BotGuideDao {
    public static List<BotGuide> getAllSpeech() {
        List<BotGuide> BotGuideSpeech = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM rooms_botguide_speech", sqlConnection);
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

        return BotGuideSpeech;
    }

    public static BotGuide getSpeechBySpeechKey(String speechKey) {
        BotGuide speech = null;

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

    private static BotGuide fill(ResultSet resultSet) throws Exception {
        return new BotGuide(
                resultSet.getString("speech_key"),
                resultSet.getString("response"),
                resultSet.getString("speech_trigger")
        );
    }
}
