package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingWordfilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingWordfilterDao {

    public static List<HousekeepingWordfilter> getAllWords(int page, String sortBy, String orderBy) {
        List<HousekeepingWordfilter> wordfilterList = new ArrayList<>();

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
                    wordfilterList.add(fill(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return wordfilterList;
    }

    public static void createWord(String word, boolean isBannable, boolean isFilterable) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "INSERT INTO wordfilter (word, is_bannable, is_filterable) VALUES (?, ?, ?)",
                    sqlConnection
            );
            preparedStatement.setString(1, word);
            preparedStatement.setBoolean(2, isBannable);
            preparedStatement.setBoolean(3, isFilterable);
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

    public static HousekeepingWordfilter getWordById(int id) {
        HousekeepingWordfilter wordfilter = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM wordfilter WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                wordfilter = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return wordfilter;
    }

    public static HousekeepingWordfilter getWordByWord(String word) {
        HousekeepingWordfilter wordfilter = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM wordfilter WHERE word = ? LIMIT 1", sqlConnection);
            preparedStatement.setString(1, word);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                wordfilter = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return wordfilter;
    }

    public static List<HousekeepingWordfilter> searchWords(String query) {
        List<HousekeepingWordfilter> words = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM wordfilter WHERE word LIKE ? OR id = ? LIMIT 50", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setString(2, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                words.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return words;
    }

    public static void saveWord(String saveWord, boolean isBannable, boolean isFilterable, int wordId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "UPDATE wordfilter SET word = ?, is_bannable = ?, is_filterable = ? WHERE id = ?",
                    sqlConnection
            );
            preparedStatement.setString(1, saveWord);
            preparedStatement.setBoolean(2, isBannable);
            preparedStatement.setBoolean(3, isFilterable);
            preparedStatement.setInt(4, wordId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingWordfilter fill(ResultSet resultSet) throws Exception {
        return new HousekeepingWordfilter(
                resultSet.getInt("id"),
                resultSet.getString("word"),
                resultSet.getBoolean("is_bannable"),
                resultSet.getBoolean("is_filterable")
        );
    }
}