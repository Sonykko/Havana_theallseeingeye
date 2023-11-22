package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.util.ConfigEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingSettingsDao {
    public static List<ConfigEntry> getAllSettingsHK() {
        List<ConfigEntry> settingsHK = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT setting, value, category FROM settings", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String key = resultSet.getString("setting");
                String value = resultSet.getString("value");

                ConfigEntry configEntryHK = new ConfigEntry(key, value);
                settingsHK.add(configEntryHK);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return settingsHK;
    }

    public static List<Map<String, Object>> getSettings(String categoryName) {
        List<Map<String, Object>> SettingsList = new ArrayList<>();

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();

                String statement = categoryName;

                preparedStatement = Storage.getStorage().prepare("SELECT * FROM settings WHERE category = '" + statement + "'", sqlConnection);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> settings = new HashMap<>();

                    settings.put("setting", resultSet.getString("setting"));
                    settings.put("value", resultSet.getString("value"));

                    SettingsList.add(settings);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }

        return SettingsList;
    }

    public static boolean CheckCategory(String categoryName) {
        boolean CategoryExists = false;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = sqlConnection.prepareStatement("SELECT * FROM settings WHERE category = ?");
            preparedStatement.setString(1, categoryName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CategoryExists = true;
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return CategoryExists;
    }

    public static List<Map<String, Object>> getSettingDescriptions() {
        List<Map<String, Object>> descriptions = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT setting, description FROM settings_desc", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> description = new HashMap<>();
                description.put("setting", resultSet.getString("setting"));
                description.put("description", resultSet.getString("description"));
                descriptions.add(description);
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return descriptions;
    }

    public static String getCategoryForSetting(String settingName) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String category = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT category FROM settings WHERE setting = ?", sqlConnection);
            preparedStatement.setString(1, settingName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                category = resultSet.getString("category");
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return category;
    }
}
