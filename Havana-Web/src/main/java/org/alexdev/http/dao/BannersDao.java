package org.alexdev.http.dao;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.promotion.Banner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BannersDao {
    public static List<Banner> getAdsBanners() {
        List<Banner> banner = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder banners = new StringBuilder();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_banners WHERE status = '1' ORDER BY order_id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                banner.add(fill(resultSet));
            }
        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return banner;
    }

    private static Banner fill(ResultSet resultSet) throws SQLException {
        return new Banner(
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
