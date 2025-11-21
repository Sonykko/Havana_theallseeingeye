package org.alexdev.http.dao.housekeeping;

import org.alexdev.duckhttpd.util.config.Settings;
import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingHotCampaign;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HousekeepingHotCampaignsDao {
    public static List<HousekeepingHotCampaign> getAllHotCampaigns() {
        List<HousekeepingHotCampaign> AllHotCampaignsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM cms_hot_campaigns ORDER BY id ASC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AllHotCampaignsList.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return AllHotCampaignsList;
    }

    public static void createHotCampaign(String title, String description, String image, String url, String urlText, int status, int orderId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO cms_hot_campaigns (title, description, image, url, url_text, status, order_id) VALUES (?, ?, ?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, image);
            preparedStatement.setString(4, url);
            preparedStatement.setString(5, urlText);
            preparedStatement.setInt(6, status);
            preparedStatement.setInt(7, orderId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deleteHotCampaign(int hotCampaignId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM cms_hot_campaigns WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, hotCampaignId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static HousekeepingHotCampaign getHotCampaignById(int hotCampaignId) {
        HousekeepingHotCampaign HotCampaignsEditList = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_hot_campaigns WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, hotCampaignId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                HotCampaignsEditList = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return HotCampaignsEditList;
    }

    public static void saveHotCampaign(HousekeepingHotCampaign hotCampaign) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_hot_campaigns SET title = ?, description = ?, image = ?, url = ?, url_text = ?, status = ?, order_id = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, hotCampaign.getTitle());
            preparedStatement.setString(2, hotCampaign.getDescription());
            preparedStatement.setString(3, hotCampaign.getImage());
            preparedStatement.setString(4, hotCampaign.getUrl());
            preparedStatement.setString(5, hotCampaign.getUrlText());
            preparedStatement.setInt(6, hotCampaign.getStatus());
            preparedStatement.setInt(7, hotCampaign.getOrderId());
            preparedStatement.setInt(8, hotCampaign.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
    public static List<String> getHotCampaignImages() {
        List<String> images = new ArrayList<String>();

        for (File file : Objects.requireNonNull(Paths.get(Settings.getInstance().getSiteDirectory(), "c_images", "hot_campaign_images_all").toFile().listFiles())) {
            if (!file.getName().contains(".gif")) {
                continue;
            }

            images.add(file.getName());
        }

        Collections.sort(images);
        return images;
    }

    private static HousekeepingHotCampaign fill(ResultSet resultSet) throws Exception {
        return new HousekeepingHotCampaign(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("image"),
                resultSet.getString("url"),
                resultSet.getString("url_text"),
                resultSet.getInt("status"),
                resultSet.getInt("order_id")
        );
    }
}
