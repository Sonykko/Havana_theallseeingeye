package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingPromotionDao {
    public static List<Map<String, Object>> getAllPickReco(int isPick) {
        List<Map<String, Object>> PickRecoList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_recommended WHERE is_staff_pick = ? ORDER BY recommended_id ASC", sqlConnection);
            preparedStatement.setInt(1, isPick);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> PickReco = new HashMap<>();
                PickReco.put("ID", resultSet.getInt("recommended_id"));
                PickReco.put("type", resultSet.getString("type"));
                PickReco.put("isPicked", resultSet.getInt("is_staff_pick"));

                PickRecoList.add(PickReco);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return PickRecoList;
    }

    public static void createPickReco(int Id, String type, int isPicked) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO cms_recommended (recommended_id, type, is_staff_pick) VALUES (?, ?, ?)", sqlConnection);
            preparedStatement.setInt(1, Id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, isPicked);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deletePickReco(int Id, String type, int isPicked) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM cms_recommended WHERE recommended_id = ? AND type = ? AND is_staff_pick = ?", sqlConnection);
            preparedStatement.setInt(1, Id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, isPicked);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static List<Map<String, Object>> EditPickReco(int Id, String type, int isPicked) {
        List<Map<String, Object>> EditStaffPickList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_recommended WHERE recommended_id = ? AND type = ? AND is_staff_pick = ?", sqlConnection);
            preparedStatement.setInt(1, Id);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, isPicked);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> EditStaffPick = new HashMap<>();
                EditStaffPick.put("ID", resultSet.getInt("recommended_id"));
                EditStaffPick.put("type", resultSet.getString("type"));
                EditStaffPick.put("isPicked", resultSet.getInt("is_staff_pick"));

                EditStaffPickList.add(EditStaffPick);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return EditStaffPickList;
    }

    public static void SavePickReco(String IdSave, String typeSave, int isPickedSave, int Id, String type, int isPicked) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_recommended SET recommended_id = ?, type = ?, is_staff_pick = ? WHERE recommended_id = ? AND type = ? AND is_staff_pick = ?", sqlConnection);
            preparedStatement.setString(1, IdSave);
            preparedStatement.setString(2, typeSave);
            preparedStatement.setInt(3, isPickedSave);
            preparedStatement.setInt(4, Id);
            preparedStatement.setString(5, type);
            preparedStatement.setInt(6, isPicked);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

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

    public static List<Map<String, Object>> getAllHotCampaigns() {
        List<Map<String, Object>> AllHotCampaignsList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT * FROM cms_hot_campaigns ORDER BY id ASC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> hotCampaign = new HashMap<>();
                hotCampaign.put("id", resultSet.getInt("id"));
                hotCampaign.put("title", resultSet.getString("title"));
                hotCampaign.put("description", resultSet.getString("description"));
                hotCampaign.put("image", resultSet.getString("image"));
                hotCampaign.put("url", resultSet.getString("url"));
                hotCampaign.put("urlText", resultSet.getString("url_text"));
                hotCampaign.put("status", resultSet.getInt("status"));
                hotCampaign.put("orderId", resultSet.getInt("order_id"));

                AllHotCampaignsList.add(hotCampaign);
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

    public static List<Map<String, Object>> EditHotCampaign(int hotCampaignId) {
        List<Map<String, Object>> HotCampaignsEditList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM cms_hot_campaigns WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, hotCampaignId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> hotCampaignEdit = new HashMap<>();
                hotCampaignEdit.put("id", resultSet.getInt("id"));
                hotCampaignEdit.put("title", resultSet.getString("title"));
                hotCampaignEdit.put("description", resultSet.getString("description"));
                hotCampaignEdit.put("image", resultSet.getString("image"));
                hotCampaignEdit.put("url", resultSet.getString("url"));
                hotCampaignEdit.put("urlText", resultSet.getString("url_text"));
                hotCampaignEdit.put("status", resultSet.getInt("status"));
                hotCampaignEdit.put("orderId", resultSet.getInt("order_id"));

                HotCampaignsEditList.add(hotCampaignEdit);
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

    public static void saveHotCampaign(String title, String description, String image, String url, String urlText, int status, int orderId, int hotCampaignId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cms_hot_campaigns SET title = ?, description = ?, image = ?, url = ?, url_text = ?, status = ?, order_id = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, image);
            preparedStatement.setString(4, url);
            preparedStatement.setString(5, urlText);
            preparedStatement.setInt(6, status);
            preparedStatement.setInt(7, orderId);
            preparedStatement.setInt(8, hotCampaignId);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
