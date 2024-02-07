package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.game.badges.BadgeDefinition;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExternalTextsDao {

    public static void updateBadges(List<BadgeDefinition> badgeDefinitions) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            for(var badge : badgeDefinitions) {

                var name = badge.getBadgeName();
                var desc = badge.getBadgeDescription();

                name = name.replace("'", "''");
                desc = desc.replace("'", "''");

                preparedStatement = Storage.getStorage().prepare("UPDATE external_texts SET text = '" + name + "' WHERE entry = 'badge_name_" + badge.getBadgeCode() + "'", sqlConnection);
                preparedStatement.execute();

                preparedStatement = Storage.getStorage().prepare("UPDATE external_texts SET text = '" + desc + "' WHERE entry = 'badge_desc_" + badge.getBadgeCode() + "'", sqlConnection);
                preparedStatement.execute();
            }
        } catch (Exception e) {
            System.out.println(e);
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<BadgeDefinition> getBadgeDefinitions() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        var badgeDefinitions = new ArrayList<BadgeDefinition>();
        var externalTexts = new HashMap<String, String>();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT entry, text FROM external_texts WHERE entry LIKE 'badge_name_%' OR entry LIKE 'badge_desc_%' ORDER BY entry", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                var entry = resultSet.getString("entry");
                var text = resultSet.getString("text");

                externalTexts.put(entry, text);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        for(var entrySet : externalTexts.entrySet()) {
            var entry = entrySet.getKey();
            var text = entrySet.getValue();

            if(text.length() == 0 || entry.length() == 0) {
                continue;
            }

            if(!entry.startsWith("badge_name_")) {
                continue;
            }

            var badgecode = entry.substring(11);

            if(badgecode.startsWith("ACH_") || badgecode.startsWith("HC")) {
                continue;
            }

            var badgename = externalTexts.get("badge_name_" + badgecode);
            var badgedesc = externalTexts.get("badge_desc_" + badgecode);

            badgeDefinitions.add(new BadgeDefinition(badgecode, badgename, badgedesc));
        }

        return badgeDefinitions;
    }

    public static List<Pair<String, String>> getFlashExternalTexts(String language) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset;

        var externalFlashTexts = new ArrayList<Pair<String, String>>();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT entry, text FROM external_texts WHERE lang = ? AND flash = 1", sqlConnection);
            preparedStatement.setString(1, language);

            resultset = preparedStatement.executeQuery();

            while(resultset.next()) {
                var entry = resultset.getString("entry");
                var text = resultset.getString("text");

                var pair = Pair.of(entry, text);

                externalFlashTexts.add(pair);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return externalFlashTexts;
    }

    public static List<Pair<String, String>> getShockwaveExternalTexts(String language) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset;

        var externalFlashTexts = new ArrayList<Pair<String, String>>();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT entry, text FROM external_texts WHERE lang = ? AND shockwave = 1", sqlConnection);
            preparedStatement.setString(1, language);

            resultset = preparedStatement.executeQuery();

            while(resultset.next()) {
                var entry = resultset.getString("entry");
                var text = resultset.getString("text");

                var pair = Pair.of(entry, text);

                externalFlashTexts.add(pair);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return externalFlashTexts;
    }
}
