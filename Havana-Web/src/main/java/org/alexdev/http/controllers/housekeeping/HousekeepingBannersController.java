package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingPromotionDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class HousekeepingBannersController {
    public static void banners (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/banners");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "articles/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());

            return;
        }

        if (client.post().contains("createBanner")) {
            String textBanner = client.post().getString("textBanner");
            String createBanner = client.post().getString("createBanner");
            String urlBanner = client.post().getString("urlBanner");
            String statusBanner = client.post().getString("statusBanner");
            String advancedBanner = client.post().getString("advancedBanner");
            String orderIdBannerStr = client.post().getString("orderIdBanner");
            int orderIdBanner = StringUtils.isNumeric(orderIdBannerStr) ? Integer.parseInt(orderIdBannerStr) : 0;

            if (!statusBanner.equals("0") && !statusBanner.equals("1") || !advancedBanner.equals("0") && !advancedBanner.equals("1")) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid status and advanced values");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
                return;
            }

            if (advancedBanner.equals("1") && createBanner.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid banner value");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
                return;
            }

            if (advancedBanner.equals("0") && textBanner.isEmpty() || createBanner.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid text and banner values");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
                return;
            }

            HousekeepingPromotionDao.createBanner(textBanner, createBanner, urlBanner, statusBanner, advancedBanner, orderIdBanner);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created Ad Banner. URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Banner has been successfully created");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
            return;
        }

        if (client.get().contains("delete")) {
            String bannerIdStr = client.get().getString("delete");
            int bannerId = StringUtils.isNumeric(bannerIdStr) ? Integer.parseInt(bannerIdStr) : 0;

            List<Map<String, Object>> checkBanner = HousekeepingPromotionDao.EditBanner(bannerId);

            if (checkBanner.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Banner not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
                return;
            }

            HousekeepingPromotionDao.deleteBanner(bannerId);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted Ad Banner with the ID " + bannerId + ". URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Banner has been successfully deleted");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
            return;
        }

        if (client.get().contains("edit")) {
            String editIdStr = client.get().getString("edit");
            int bannerId = StringUtils.isNumeric(editIdStr) ? Integer.parseInt(editIdStr) : 0;

            List<Map<String, Object>> checkBanner = HousekeepingPromotionDao.EditBanner(bannerId);

            if (checkBanner.isEmpty()) {
                tpl.set("isBannerEdit", false);
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Banner not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
                return;
            }

            tpl.set("BannerEdit", HousekeepingPromotionDao.EditBanner(Integer.parseInt(editIdStr)));
            tpl.set("isBannerEdit", true);
        } else {
            tpl.set("isBannerEdit", false);
        }

        if (client.post().contains("saveBanner")) {
            String textBanner = client.post().getString("textBanner");
            String saveBanner = client.post().getString("saveBanner");
            String urlBanner = client.post().getString("urlBanner");
            String statusBanner = client.post().getString("statusBanner");
            String advancedBanner = client.post().getString("advancedBanner");
            String orderIdBannerStr = client.post().getString("orderIdBanner");
            int orderIdBanner = StringUtils.isNumeric(orderIdBannerStr) ? Integer.parseInt(orderIdBannerStr) : 0;

            String bannerIdStr = client.get().getString("edit");
            int bannerId = StringUtils.isNumeric(bannerIdStr) ? Integer.parseInt(bannerIdStr) : 0;

            List<Map<String, Object>> checkBanner = HousekeepingPromotionDao.EditBanner(bannerId);

            if (checkBanner.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "The Banner not exists");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
                return;
            }

            if (!statusBanner.equals("0") && !statusBanner.equals("1") || !advancedBanner.equals("0") && !advancedBanner.equals("1")) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid status and advanced values");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners?edit="+ bannerId);
                return;
            }

            if (advancedBanner.equals("1") && saveBanner.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid banner value");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners?edit="+ bannerId);
                return;
            }

            if (advancedBanner.equals("0") && textBanner.isEmpty() || saveBanner.isEmpty()) {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid text and banner values");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners?edit="+ bannerId);
                return;
            }

            HousekeepingPromotionDao.saveBanner(textBanner, saveBanner, urlBanner, statusBanner, advancedBanner, orderIdBanner, bannerId);
            HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edit Ad Banner with the ID " + bannerId + ". URL: " + client.request().uri(), client.getIpAddress());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "The Banner has been successfully saved");
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");
            return;
        }

        tpl.set("pageName", "Ads banners");
        tpl.set("Banners", HousekeepingPromotionDao.getAllBanners());
        tpl.render();

        client.session().delete("alertMessage");
    }
}
