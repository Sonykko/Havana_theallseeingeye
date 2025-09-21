package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingCataloguePage;
import org.alexdev.http.game.housekeeping.HousekeepingCatalogueParent;
import org.alexdev.http.game.housekeeping.HousekeepingRankVar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingCatalogueDao {
    public static List<HousekeepingCataloguePage> searchCataloguePage(String type, String field, String input) {
        List<HousekeepingCataloguePage> searchPagesList = new ArrayList<>();
        List<HousekeepingRankVar> allRanksList = HousekeepingRankDao.getAllRanksVars();

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

                searchPagesList.add(fillPage(resultSet, allRanksList));
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

    public static List<HousekeepingCataloguePage> getCataloguePages(String type, int pageId, int page) {
        List<HousekeepingCataloguePage> cataloguePagesList = new ArrayList<>();
        List<HousekeepingRankVar> allRanksList = HousekeepingRankDao.getAllRanksVars();

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
                    cataloguePagesList.add(fillPage(resultSet, allRanksList));
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

    private static String findRankName(int rankId, List<HousekeepingRankVar> allRanksList) {
        for (HousekeepingRankVar rank : allRanksList) {
            if (rank.getId() == rankId) {
                return rank.getName();
            }
        }
        return null;
    }

    public static List<HousekeepingCatalogueParent> getAllParentNames() {
        List<HousekeepingCatalogueParent> allParentNamesList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM catalogue_pages WHERE layout != 'frontpage3' ORDER BY name ASC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allParentNamesList.add(fillParent(resultSet));
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

    public static void updatePage(HousekeepingCataloguePage cataloguePage, int originalPageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE catalogue_pages SET id = ?, parent_id = ?, order_id = ?, min_role = ?, is_navigatable = ?, is_club_only = ?, name = ?, icon = ?, colour = ?, layout = ?, images = ?, texts = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, cataloguePage.getId());
            preparedStatement.setInt(2, cataloguePage.getParentId());
            preparedStatement.setInt(3, cataloguePage.getOrderId());
            preparedStatement.setInt(4, cataloguePage.getMinRoleId());
            preparedStatement.setInt(5, cataloguePage.getIsNavigatable());
            preparedStatement.setInt(6, cataloguePage.getIsClubOnly());
            preparedStatement.setString(7, cataloguePage.getName());
            preparedStatement.setInt(8, cataloguePage.getIcon());
            preparedStatement.setInt(9, cataloguePage.getColour());
            preparedStatement.setString(10, cataloguePage.getLayout());
            preparedStatement.setString(11, cataloguePage.getImages());
            preparedStatement.setString(12, cataloguePage.getTexts());
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
            preparedStatement = Storage.getStorage().prepare("INSERT INTO catalogue_pages (old_id, parent_id, order_id, min_role, is_navigatable, is_club_only, name, icon, colour, layout, images, texts) VALUES (0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", sqlConnection);
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

    private static HousekeepingCataloguePage fillPage(ResultSet resultSet, List<HousekeepingRankVar> allRanksList) throws Exception {
        int minRoleId = resultSet.getInt("min_role");
        String minRoleName = findRankName(minRoleId, allRanksList);

        return new HousekeepingCataloguePage(
                resultSet.getInt("id"),
                resultSet.getInt("parent_id"),
                resultSet.getInt("order_id"),
                minRoleName,
                minRoleId,
                resultSet.getInt("is_navigatable"),
                resultSet.getInt("is_club_only"),
                resultSet.getString("name"),
                resultSet.getInt("icon"),
                resultSet.getInt("colour"),
                resultSet.getString("layout"),
                resultSet.getString("images"),
                resultSet.getString("texts"),
                resultSet.getString("seasonal_start"),
                resultSet.getInt("seasonal_length")
        );
    }

    private static HousekeepingCatalogueParent fillParent(ResultSet resultSet) throws Exception {
        return new HousekeepingCatalogueParent(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
