package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingGamesRanks;
import org.alexdev.http.game.housekeeping.enums.GameType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HousekeepingGamesRanksDao {
    public static void create(String title, GameType gameType, int minPoints, int maxPoints) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO games_ranks (type, title, min_points, max_points) VALUES (?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, String.valueOf(gameType));
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, minPoints);
            preparedStatement.setInt(4, maxPoints);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<HousekeepingGamesRanks> getGamesRanksByPage(int page) {
        List<HousekeepingGamesRanks> GamesRanksList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {
            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatement = Storage.getStorage().prepare("SELECT * FROM games_ranks ORDER BY id DESC LIMIT ? OFFSET ?", sqlConnection);
                preparedStatement.setInt(1, rows);
                preparedStatement.setInt(2, nextOffset);

                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    GamesRanksList.add(fill(resultSet));
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }

        }

        return GamesRanksList;
    }

    public static HousekeepingGamesRanks getGameRankById(int id) {
        HousekeepingGamesRanks gameRank = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM games_ranks WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                gameRank = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return gameRank;
    }


    public static List<HousekeepingGamesRanks> searchGamesRanks(String query) {
        List<HousekeepingGamesRanks> gamesRanks = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM games_ranks WHERE type LIKE ? OR title LIKE ? OR id = ? LIMIT 20", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setString(2, query + "%");
            preparedStatement.setString(3, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gamesRanks.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return gamesRanks;
    }

    public static void save(HousekeepingGamesRanks gameRank) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE games_ranks SET type = ?, title = ?, min_points = ?, max_points = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, String.valueOf(gameRank.getType()));
            preparedStatement.setString(2, gameRank.getTitle());
            preparedStatement.setInt(3, gameRank.getMinPoints());
            preparedStatement.setInt(4, gameRank.getMaxPoints());
            preparedStatement.setInt(5, gameRank.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void delete(int rankId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM games_ranks WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, rankId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingGamesRanks fill(ResultSet resultSet) throws Exception {
        String typeStr = resultSet.getString("type");

        GameType type = Arrays.stream(GameType.values())
                .filter(gt -> gt.name().equalsIgnoreCase(typeStr))
                .findFirst()
                .orElse(null);

        return new HousekeepingGamesRanks(
                resultSet.getInt("id"),
                type,
                resultSet.getString("title"),
                resultSet.getInt("min_points"),
                resultSet.getInt("max_points")
        );
    }
}

