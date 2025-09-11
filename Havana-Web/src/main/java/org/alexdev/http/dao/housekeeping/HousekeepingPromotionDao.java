package org.alexdev.http.dao.housekeeping;

import org.alexdev.duckhttpd.util.config.Settings;
import org.alexdev.havana.dao.Storage;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class HousekeepingPromotionDao {
    public static List<Map<String, Object>> getAllBanners() {
        List<Map<String, Object>> BannersList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_banners ORDER BY order_id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> Banner = new HashMap<>();
                Banner.put("id", resultSet.getInt("id"));
                Banner.put("text", resultSet.getString("text"));
                Banner.put("banner", resultSet.getString("banner"));
                Banner.put("url", resultSet.getString("url"));
                Banner.put("status", resultSet.getInt("status"));
                Banner.put("advanced", resultSet.getInt("advanced"));
                Banner.put("orderId", resultSet.getInt("order_id"));

                BannersList.add(Banner);
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

    public static List<Map<String, Object>> EditBanner(int bannerId) {
        List<Map<String, Object>> BannersEditList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_banners WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, bannerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> BannerEdit = new HashMap<>();
                BannerEdit.put("id", resultSet.getInt("id"));
                BannerEdit.put("text", resultSet.getString("text"));
                BannerEdit.put("banner", resultSet.getString("banner"));
                BannerEdit.put("url", resultSet.getString("url"));
                BannerEdit.put("status", resultSet.getInt("status"));
                BannerEdit.put("advanced", resultSet.getInt("advanced"));
                BannerEdit.put("orderId", resultSet.getInt("order_id"));

                BannersEditList.add(BannerEdit);
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
}
