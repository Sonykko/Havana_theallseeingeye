package org.alexdev.http.dao;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.promotion.HotCampaign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HotCampaignsDao {
    public static List<HotCampaign> getAllHotCampaigns() {
        List<HotCampaign> AllHotCampaignsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM cms_hot_campaigns WHERE status = '1' ORDER BY order_id ASC", sqlConnection);

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

    private static HotCampaign fill(ResultSet resultSet) throws Exception {
        return new HotCampaign(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("image"),
                resultSet.getString("url"),
                resultSet.getString("url_text")
        );
    }
}
