package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingPromotionDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

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

            if (orderIdBanner > 0) {
                HousekeepingPromotionDao.createBanner(textBanner, createBanner, urlBanner, statusBanner, advancedBanner, orderIdBanner);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Banner has been successfully created");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");

                return;
            }

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Banner order ID");
        }

        if (client.get().contains("delete")) {
            String bannerIdStr = client.get().getString("delete");
            int bannerId = StringUtils.isNumeric(bannerIdStr) ? Integer.parseInt(bannerIdStr) : 0;

            if (bannerId > 0) {
                HousekeepingPromotionDao.deleteBanner(bannerId);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Banner has been successfully deleted");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");

                return;
            } else {
                client.session().set("alertColour", "danger");
                client.session().set("alertMessage", "Please enter a valid Banner ID");
            }
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Banner ID");
        }

        if (client.get().contains("edit")) {
            String editIdStr = client.get().getString("edit");
            if (StringUtils.isNumeric(editIdStr)) {
                tpl.set("BannerEdit", HousekeepingPromotionDao.EditBanner(Integer.parseInt(editIdStr)));
                tpl.set("isBannerEdit", true);
            } else {
                tpl.set("isBannerEdit", false);
            }
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

            String bannerIdStr = client.post().getString("edit");
            int bannerId = StringUtils.isNumeric(bannerIdStr) ? Integer.parseInt(bannerIdStr) : 0;


            if (orderIdBanner > 0 && bannerId > 0) {
                HousekeepingPromotionDao.saveBanner(textBanner, saveBanner, urlBanner, statusBanner, advancedBanner, orderIdBanner, bannerId);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "The Banner has been successfully saved");
                client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/banners");

                return;
            }

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Banner order ID");
        }

        tpl.set("pageName", "Ads banners");
        tpl.set("Banners", HousekeepingPromotionDao.getAllBanners());
        tpl.render();

        client.session().delete("alertMessage");
    }
}