package org.alexdev.http.dao;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionDao {
    public static String getAdsBanners() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder banners = new StringBuilder();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_banners WHERE status = '1' ORDER BY order_id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString("advanced").equals("1")) {
                    banners.append(resultSet.getString("banner") + "<br />");
                } else {
                    banners.append("<a target=\"_blank\" href=\"" + resultSet.getString("url") + "\"><img src=\"" + resultSet.getString("banner") + "\"></a><br />");
                    banners.append("<a target=\"_blank\" href=\"" + resultSet.getString("url") + "\">" + resultSet.getString("text") + "</a><br />");
                }
            }
        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return banners.toString();
    }

    public static List<Map<String, Object>> getAllHotCampaigns() {
        List<Map<String, Object>> AllHotCampaignsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM cms_hot_campaigns WHERE status = '1' ORDER BY order_id ASC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> HotCampaign = new HashMap<>();
                HotCampaign.put("id", resultSet.getInt("id"));
                HotCampaign.put("title", resultSet.getString("title"));
                HotCampaign.put("description", resultSet.getString("description"));
                HotCampaign.put("image", resultSet.getString("image"));
                HotCampaign.put("url", resultSet.getString("url"));
                HotCampaign.put("urlText", resultSet.getString("url_text"));

                AllHotCampaignsList.add(HotCampaign);
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
}