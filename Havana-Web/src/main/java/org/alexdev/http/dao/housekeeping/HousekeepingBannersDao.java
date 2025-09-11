package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingBanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingBannersDao {
    public static List<HousekeepingBanner> getAllBanners() {
        List<HousekeepingBanner> BannersList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_banners ORDER BY order_id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BannersList.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return BannersList;
    }

    public static void createBanner(String textBanner, String createBanner, String urlBanner, String statusBanner, String advancedBanner, int orderIdBanner) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO cms_banners (text, banner, url, status, advanced, order_id) VALUES (?, ?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, textBanner);
            preparedStatement.setString(2, createBanner);
            preparedStatement.setString(3, urlBanner);
            preparedStatement.setString(4, statusBanner);
            preparedStatement.setString(5, advancedBanner);
            preparedStatement.setInt(6, orderIdBanner);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deleteBanner(int bannerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM cms_banners WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, bannerId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static HousekeepingBanner getBannerById(int bannerId) {
        HousekeepingBanner BannersEditList = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_banners WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, bannerId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                BannersEditList = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return BannersEditList;
    }

    public static void saveBanner(String textBanner, String saveBanner, String urlBanner, String statusBanner, String advancedBanner, int orderIdBanner, int bannerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_banners SET text = ?, banner = ?, url = ?, status = ?, advanced = ?, order_id = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, textBanner);
            preparedStatement.setString(2, saveBanner);
            preparedStatement.setString(3, urlBanner);
            preparedStatement.setString(4, statusBanner);
            preparedStatement.setString(5, advancedBanner);
            preparedStatement.setInt(6, orderIdBanner);
            preparedStatement.setInt(7, bannerId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static HousekeepingBanner fill(ResultSet resultSet) throws Exception {
        return new HousekeepingBanner(
                resultSet.getInt("id"),
                resultSet.getString("text"),
                resultSet.getString("banner"),
                resultSet.getString("url"),
                resultSet.getInt("status"),
                resultSet.getInt("advanced"),
                resultSet.getInt("order_id")
        );
    }
}
