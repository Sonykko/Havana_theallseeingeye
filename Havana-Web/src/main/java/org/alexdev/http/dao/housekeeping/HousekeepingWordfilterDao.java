package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingWordfilterDao {
    public static List<Map<String, Object>> getAllWords(int page, String sortBy, String orderBy) {
        List<Map<String, Object>> WordfilterList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM wordfilter ORDER BY " + sortBy + " " + orderBy + " LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> Word = new HashMap<>();
                    Word.put("id", resultSet.getInt("id"));
                    Word.put("wordFilter", resultSet.getString("word"));
                    Word.put("isBannable", resultSet.getInt("is_bannable"));
                    Word.put("isFilterable", resultSet.getInt("is_filterable"));

                    WordfilterList.add(Word);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return WordfilterList;
    }

    public static void createWord(String word, int isBannable, int isFilterable) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO wordfilter (word, is_bannable, is_filterable) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, word);
            preparedStatement.setInt(2, isBannable);
            preparedStatement.setInt(3, isFilterable);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deleteWord(int wordId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM wordfilter WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, wordId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<Map<String, Object>> editWord(int wordId) {
        List<Map<String, Object>> WordfilterEditList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM wordfilter WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, wordId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> WordEdit = new HashMap<>();
                WordEdit.put("id", resultSet.getInt("id"));
                WordEdit.put("wordFilter", resultSet.getString("word"));
                WordEdit.put("isBannable", resultSet.getInt("is_bannable"));
                WordEdit.put("isFilterable", resultSet.getInt("is_filterable"));

                WordfilterEditList.add(WordEdit);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return WordfilterEditList;
    }

    public static void saveWord(String saveWord, int isBannable, int isFilterable, int wordId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE wordfilter SET word = ?, is_bannable = ?, is_filterable = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, saveWord);
            preparedStatement.setInt(2, isBannable);
            preparedStatement.setInt(3, isFilterable);
            preparedStatement.setInt(4, wordId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static boolean CheckWord(String word) {
        boolean wordExists = false;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM wordfilter WHERE word = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, word);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                wordExists = true;
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return wordExists;
    }
}
