package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingHotCampaignsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class HousekeepingHotCampaignsController {
    public static void hot_campaigns(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/hot_campaigns");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "articles/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().contains("createHotCampaign")) {
            createHotCampaign(client, playerDetails);
            return;
        }

        if (client.get().contains("delete")) {
            deleteHotCampaign(client, playerDetails);
            return;
        }

        if (client.get().contains("edit")) {
            editHotCampaign(client, tpl);
        } else {
            tpl.set("isHotCampaignEdit", false);
        }

        if (client.post().contains("saveHotCampaign")) {
            saveHotCampaign(client, playerDetails);
            return;
        }

        List<String> images = HousekeepingHotCampaignsDao.getHotCampaignImages();

        tpl.set("pageName", "Hot Campaigns");
        tpl.set("HotCampaigns", HousekeepingHotCampaignsDao.getAllHotCampaigns());
        tpl.set("images", images);
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getHotCampaignsPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/hot_campaigns";
    }

    public static void createHotCampaign (WebConnection client, PlayerDetails playerDetails) {
        String title = client.post().getString("title");
        String description = client.post().getString("description");
        String createHotCampaign = client.post().getString("createHotCampaign");
        String url = client.post().getString("url");
        String urlText = client.post().getString("urlText");

        String statusString = client.post().getString("status");
        String orderIdString = client.post().getString("orderId");

        if (!StringUtils.isNumeric(orderIdString)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hot Campaign order ID");
            client.redirect(getHotCampaignsPath());
            return;
        }

        int status = Integer.parseInt(statusString);
        int orderId = Integer.parseInt(orderIdString);

        if (title.isEmpty() || description.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hot Campaign title and description");
            client.redirect(getHotCampaignsPath());
            return;
        }


        HousekeepingHotCampaignsDao.createHotCampaign(title, description, createHotCampaign, url, urlText, status, orderId);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created Hot Campaign. URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Hot Campaign has been successfully created");
        client.redirect(getHotCampaignsPath());
    }
    
    public static void deleteHotCampaign (WebConnection client, PlayerDetails playerDetails) {
        String hotCampaignIdString = client.get().getString("delete");

        if (!StringUtils.isNumeric(hotCampaignIdString)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hot Campaign ID");
            client.redirect(getHotCampaignsPath());
            return;
        }

        int hotCampaignId = Integer.parseInt(hotCampaignIdString);

        var hotCampaign = HousekeepingHotCampaignsDao.getHotCampaignById(hotCampaignId);

        if (hotCampaign == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Hot Campaign ID not exists");
            client.redirect(getHotCampaignsPath());
            return;
        }

        HousekeepingHotCampaignsDao.deleteHotCampaign(hotCampaignId);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted Hot Campaign with the ID " + hotCampaignId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Hot Campaign has been successfully deleted");
        client.redirect(getHotCampaignsPath());
    }
    
    public static void editHotCampaign (WebConnection client, Template tpl) {
        String editIdString = client.get().getString("edit");

        if (!StringUtils.isNumeric(editIdString)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hot Campaign ID");
            client.redirect(getHotCampaignsPath());
            tpl.set("isHotCampaignEdit", false);
            return;
        }

        int editId = Integer.parseInt(editIdString);

        var hotCampaign = HousekeepingHotCampaignsDao.getHotCampaignById(editId);

        if (hotCampaign == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Hot Campaign ID not exists");
            client.redirect(getHotCampaignsPath());
            tpl.set("isHotCampaignEdit", false);
            return;
        }

        tpl.set("HotCampaignEdit", hotCampaign);
        tpl.set("isHotCampaignEdit", true);
    }
    
    public static void saveHotCampaign (WebConnection client, PlayerDetails playerDetails) {
        String title = client.post().getString("title");
        String description = client.post().getString("description");
        String saveHotCampaign = client.post().getString("saveHotCampaign");
        String url = client.post().getString("url");
        String urlText = client.post().getString("urlText");

        String statusString = client.post().getString("status");
        String orderIdString = client.post().getString("orderId");
        String editIdString = client.get().getString("edit");

        if (!StringUtils.isNumeric(orderIdString)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hot Campaign order ID");
            client.redirect(getHotCampaignsPath() + "?edit="+ editIdString);
            return;
        }

        int status = Integer.parseInt(statusString);
        int orderId = Integer.parseInt(orderIdString);
        int hotCampaignId = Integer.parseInt(editIdString);

        var hotCampaign = HousekeepingHotCampaignsDao.getHotCampaignById(hotCampaignId);

        if (hotCampaign == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Hot Campaign ID not exists");
            client.redirect(getHotCampaignsPath() + "?edit="+ hotCampaignId);
            return;
        }

        if (title.isEmpty() || description.isEmpty()) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Hot Campaign title and description");
            client.redirect(getHotCampaignsPath() + "?edit="+ hotCampaignId);
            return;
        }

        hotCampaign.setTitle(title);
        hotCampaign.setDescription(description);
        hotCampaign.setImage(saveHotCampaign);
        hotCampaign.setUrl(url);
        hotCampaign.setUrlText(urlText);
        hotCampaign.setStatus(status);
        hotCampaign.setOrderId(orderId);
        hotCampaign.setId(hotCampaignId);

        HousekeepingHotCampaignsDao.saveHotCampaign(hotCampaign);

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Edited Hot Campaign with the ID " + hotCampaignId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Hot Campaign has been successfully saved");
        client.redirect(getHotCampaignsPath());
    }
}
