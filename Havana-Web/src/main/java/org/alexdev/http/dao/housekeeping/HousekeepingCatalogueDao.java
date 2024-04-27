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
        List<Map<String, Object>> allRanksList = HousekeepingPlayerDao.getAllRanks();

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
                int minRoleId = resultSet.getInt("min_role");
                String minRoleName = findRankName(minRoleId, allRanksList);
                CataloguePagesSearch.put("min_role", minRoleName);
                CataloguePagesSearch.put("min_role_ID", minRoleId);
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
        List<Map<String, Object>> allRanksList = HousekeepingPlayerDao.getAllRanks();

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
                    int minRoleId = resultSet.getInt("min_role");
                    String minRoleName = findRankName(minRoleId, allRanksList);
                    CataloguePages.put("min_role", minRoleName);
                    CataloguePages.put("min_role_ID", minRoleId);
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

    private static String findRankName(int rankId, List<Map<String, Object>> allRanksList) {
        for (Map<String, Object> rank : allRanksList) {
            if ((int) rank.get("id") == rankId) {
                return (String) rank.get("name");
            }
        }
        return null;
    }

    public static List<Map<String, Object>> getAllParentNames() {
        List<Map<String, Object>> allParentNamesList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' ORDER BY name ASC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> parentNames = new HashMap<>();
                parentNames.put("id", resultSet.getInt("id"));
                parentNames.put("name", resultSet.getString("name"));

                allParentNamesList.add(parentNames);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return allParentNamesList;
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

    public static void createCataloguePage(String parentId, String orderId, String minRank, String isNavigatable, String isHCOnly, String name, String icon, String colour, String layout, String images, String texts) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO catalogue_pages (parent_id, order_id, min_role, is_navigatable, is_club_only, name, icon, colour, layout, images, texts) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, Integer.parseInt(parentId));
            preparedStatement.setInt(2, Integer.parseInt(orderId));
            preparedStatement.setInt(3, Integer.parseInt(minRank));
            preparedStatement.setInt(4, Integer.parseInt(isNavigatable));
            preparedStatement.setInt(5, Integer.parseInt(isHCOnly));
            preparedStatement.setString(6, name);
            preparedStatement.setInt(7, Integer.parseInt(icon));
            preparedStatement.setInt(8, Integer.parseInt(colour));
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

    public static void copyPage(int pageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            DatabaseMetaData metaData = sqlConnection.getMetaData();
            ResultSet rsColumns = metaData.getColumns(null, null, "catalogue_pages", null);

            StringBuilder columnsBuilder = new StringBuilder();
            while (rsColumns.next()) {
                String columnName = rsColumns.getString("COLUMN_NAME");
                if (!columnName.equals("id")) {
                    columnsBuilder.append(columnName).append(",");
                }
            }
            rsColumns.close();
            String columns = columnsBuilder.substring(0, columnsBuilder.length() - 1);

            String sql = "INSERT INTO catalogue_pages (" + columns + ") " +
                    "SELECT " + columns + " " +
                    "FROM catalogue_pages " +
                    "WHERE id = ?";

            preparedStatement = sqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, pageId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
