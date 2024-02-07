package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {

    public static List<String> getAllCountryBadges() {
        List<String> countryBadges = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT badge FROM countries", sqlConnection);
            resultSet =  preparedStatement.executeQuery();

            while (resultSet.next()) {
                countryBadges.add(resultSet.getString("badge"));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
            Storage.closeSilently(resultSet);
        }

        return countryBadges;
    }

    public static String getCountryBadge(String countryName) {
        String countryBadge = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT badge FROM countries WHERE country_name = ?", sqlConnection);
            preparedStatement.setString(1, countryName);
            resultSet =  preparedStatement.executeQuery();

            if (resultSet.next()) {
                countryBadge = resultSet.getString("badge");
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
            Storage.closeSilently(resultSet);
        }

        return countryBadge;
    }
}
