package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingPickReco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingPickRecoDao {
    public static List<HousekeepingPickReco> getAllPickReco(int isPick) {
        List<HousekeepingPickReco> PickRecoList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_recommended WHERE is_staff_pick = ? ORDER BY recommended_id ASC", sqlConnection);
            preparedStatement.setInt(1, isPick);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PickRecoList.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return PickRecoList;
    }

    public static void createPickReco(int Id, String type, int isPicked) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO cms_recommended (recommended_id, type, is_staff_pick) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, Id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, isPicked);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deletePickReco(int Id, String type, int isPicked) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM cms_recommended WHERE recommended_id = ? AND type = ? AND is_staff_pick = ?", sqlConnection);
            preparedStatement.setInt(1, Id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, isPicked);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static HousekeepingPickReco getPickReco(int Id, String type, int isPicked) {
        HousekeepingPickReco pickreco = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_recommended WHERE recommended_id = ? AND type = ? AND is_staff_pick = ?", sqlConnection);
            preparedStatement.setInt(1, Id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, isPicked);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                pickreco = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return pickreco;
    }

    public static void SavePickReco(String IdSave, String typeSave, int isPickedSave, int Id, String type, int isPicked) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_recommended SET recommended_id = ?, type = ?, is_staff_pick = ? WHERE recommended_id = ? AND type = ? AND is_staff_pick = ?", sqlConnection);
            preparedStatement.setString(1, IdSave);
            preparedStatement.setString(2, typeSave);
            preparedStatement.setInt(3, isPickedSave);
            preparedStatement.setInt(4, Id);
            preparedStatement.setString(5, type);
            preparedStatement.setInt(6, isPicked);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingPickReco fill(ResultSet resultSet) throws Exception {
        return new HousekeepingPickReco(
                resultSet.getInt("recommended_id"),
                resultSet.getString("type"),
                resultSet.getString("is_staff_pick")
        );
    }
}
