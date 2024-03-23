package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingCatalogueDao {
    public static List<Map<String, Object>> searchCataloguePage(String type, String field, String input) {
        List<Map<String, Object>> searchPagesList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            String query = "";

            if (type.equals("contains")) {
                query = "SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' " +
                        "AND " + field + " LIKE ? " +
                        "ORDER BY id ASC LIMIT 50";
                input = "%" + input + "%";
            } else if (type.equals("starts_with")) {
                query = "SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' " +
                        "AND " + field + " LIKE ? " +
                        "ORDER BY id ASC LIMIT 50";
                input = input + "%";
            } else if (type.equals("ends_with")) {
                query = "SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' " +
                        "AND " + field + " LIKE ? " +
                        "ORDER BY id ASC LIMIT 50";
                input = "%" + input;
            } else {
                query = "SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' " +
                        "AND " + field + " = ? " +
                        "ORDER BY id ASC LIMIT 50";
            }

            preparedStatement = Storage.getStorage().prepare(query, sqlConnection);
            preparedStatement.setString(1, input);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> CataloguePagesSearch = new HashMap<>();
                CataloguePagesSearch.put("id", resultSet.getInt("id"));
                CataloguePagesSearch.put("parent_id", resultSet.getInt("parent_id"));
                CataloguePagesSearch.put("order_id", resultSet.getInt("order_id"));
                CataloguePagesSearch.put("min_role", resultSet.getInt("min_role"));
                CataloguePagesSearch.put("is_navigatable", resultSet.getInt("is_navigatable"));
                CataloguePagesSearch.put("is_club_only", resultSet.getInt("is_club_only"));
                CataloguePagesSearch.put("name", resultSet.getString("name"));
                CataloguePagesSearch.put("icon", resultSet.getInt("icon"));
                CataloguePagesSearch.put("colour", resultSet.getInt("colour"));
                CataloguePagesSearch.put("layout", resultSet.getString("layout"));
                CataloguePagesSearch.put("images", resultSet.getString("images"));
                CataloguePagesSearch.put("texts", resultSet.getString("texts"));
                CataloguePagesSearch.put("seasonal_start", resultSet.getString("seasonal_start"));
                CataloguePagesSearch.put("seasonal_length", resultSet.getInt("seasonal_length"));

                searchPagesList.add(CataloguePagesSearch);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return searchPagesList;
    }

    public static List<Map<String, Object>> getCataloguePages(String type, int pageId, int page) {
        List<Map<String, Object>> cataloguePagesList = new ArrayList<>();

        int rows = 20;
        int nextOffset = page * rows;

        if (nextOffset >= 0) {

            Connection sqlConnection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                sqlConnection = Storage.getStorage().getConnection();
                if (type == "all") {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' ORDER BY id ASC LIMIT ? OFFSET ?", sqlConnection
                    );
                    preparedStatement.setInt(1, rows);
                    preparedStatement.setInt(2, nextOffset);
                }

                if (type == "edit") {
                    preparedStatement = Storage.getStorage().prepare("SELECT * FROM catalogue_pages WHERE id = ? AND layout != 'frontpage3'", sqlConnection
                    );
                    preparedStatement.setInt(1, pageId);
                }


                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> CataloguePages = new HashMap<>();
                    CataloguePages.put("id", resultSet.getInt("id"));
                    CataloguePages.put("parent_id", resultSet.getInt("parent_id"));
                    CataloguePages.put("order_id", resultSet.getInt("order_id"));
                    CataloguePages.put("min_role", resultSet.getInt("min_role"));
                    CataloguePages.put("is_navigatable", resultSet.getInt("is_navigatable"));
                    CataloguePages.put("is_club_only", resultSet.getInt("is_club_only"));
                    CataloguePages.put("name", resultSet.getString("name"));
                    CataloguePages.put("icon", resultSet.getInt("icon"));
                    CataloguePages.put("colour", resultSet.getInt("colour"));
                    CataloguePages.put("layout", resultSet.getString("layout"));
                    CataloguePages.put("images", resultSet.getString("images"));
                    CataloguePages.put("texts", resultSet.getString("texts"));
                    CataloguePages.put("seasonal_start", resultSet.getString("seasonal_start"));
                    CataloguePages.put("seasonal_length", resultSet.getInt("seasonal_length"));

                    cataloguePagesList.add(CataloguePages);
                }

            } catch (Exception e) {
                Storage.logError(e);
            } finally {
                Storage.closeSilently(resultSet);
                Storage.closeSilently(preparedStatement);
                Storage.closeSilently(sqlConnection);
            }
        }

        return cataloguePagesList;
    }

    public static void deletePage(int pageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM catalogue_pages WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, pageId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void updatePage(int pageId, int parentId, int orderId, int minRank, int isNavigatable, int isHCOnly, String name, int icon, int colour, String layout, String images, String texts, int originalPageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE catalogue_pages SET id = ?, parent_id = ?, order_id = ?, min_role = ?, is_navigatable = ?, is_club_only = ?, name = ?, icon = ?, colour = ?, layout = ?, images = ?, texts = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, pageId);
            preparedStatement.setInt(2, parentId);
            preparedStatement.setInt(3, orderId);
            preparedStatement.setInt(4, minRank);
            preparedStatement.setInt(5, isNavigatable);
            preparedStatement.setInt(6, isHCOnly);
            preparedStatement.setString(7, name);
            preparedStatement.setInt(8, icon);
            preparedStatement.setInt(9, colour);
            preparedStatement.setString(10, layout);
            preparedStatement.setString(11, images);
            preparedStatement.setString(12, texts);
            preparedStatement.setInt(13, originalPageId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void createCataloguePage(int parentId, int orderId, int minRank, int isNavigatable, int isHCOnly, String name, int icon, int colour, String layout, String images, String texts) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO catalogue_pages (parent_id, order_id, min_role, is_navigatable, is_club_only, name, icon, colour, layout, images, texts) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, parentId);
            preparedStatement.setInt(2, orderId);
            preparedStatement.setInt(3, minRank);
            preparedStatement.setInt(4, isNavigatable);
            preparedStatement.setInt(5, isHCOnly);
            preparedStatement.setString(6, name);
            preparedStatement.setInt(7, icon);
            preparedStatement.setInt(8, colour);
            preparedStatement.setString(9, layout);
            preparedStatement.setString(10, images);
            preparedStatement.setString(11, texts);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
