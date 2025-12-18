package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCatalogueDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingRankDao;
import org.alexdev.http.game.housekeeping.HousekeepingCataloguePage;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HousekeepingCataloguePagesController {
    public static void pages (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/catalogue_pages");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "catalogue/edit_frontpage")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().queries().size() > 0 && client.post().contains("searchField")) {
            searchPage(client, tpl);
            return;
        }

        if (client.get().contains("delete")) {
            deletePage(client, playerDetails);
            return;
        }

        if (client.get().contains("edit")) {
            editPage(client, tpl);
        } else {
            tpl.set("isPageEdit", false);
        }

        if (client.post().contains("editId")) {
            savePage(client, playerDetails);
            return;
        }

        if (client.get().contains("copy")) {
            copyPage(client, playerDetails);
            return;
        }

        int currentPage = 0;

        if (client.get().contains("page")) {
            currentPage = Integer.parseInt(client.get().getString("page"));
        }

        tpl.set("pageName", "Manage catalogue pages");
        tpl.set("allRanks", HousekeepingRankDao.getAllRanksVars());
        tpl.set("ParentNames", HousekeepingCatalogueDao.getAllParentNames());
        tpl.set("pages", HousekeepingCatalogueDao.getCataloguePages("all", 0, currentPage));
        tpl.set("nextCatalogPages", HousekeepingCatalogueDao.getCataloguePages("all", 0, currentPage + 1));
        tpl.set("previousCatalogPages", HousekeepingCatalogueDao.getCataloguePages("all", 0, currentPage - 1));
        tpl.set("page", currentPage);
        tpl.render();

        client.session().delete("alertMessage");
    }

    public static void refreshCataloguePages() {
        RconUtil.sendCommand(RconHeader.REFRESH_CATALOGUE_PAGES, new HashMap<>());
    }

    private static String getManagePagesPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/catalogue/pages";
    }

    public static void searchPage (WebConnection client, Template tpl) {
        String[] fieldCheck = new String[]{"searchField", "searchQuery", "searchType" };

        for (String field : fieldCheck) {
            if (client.post().contains(field) && client.post().getString(field).length() > 0) {
                continue;
            }

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "You need to enter all fields");
            tpl.render();

            // Delete alert after it's been rendered
            client.session().delete("alertMessage");
            return;
        }

        String field = client.post().getString("searchField");
        String input = client.post().getString("searchQuery");
        String type = client.post().getString("searchType");

        List<String> whitelistColumns = new ArrayList<>();
        whitelistColumns.add("id");
        whitelistColumns.add("parent_id");
        whitelistColumns.add("min_role");
        whitelistColumns.add("name");
        whitelistColumns.add("layout");
        whitelistColumns.add("images");
        whitelistColumns.add("texts");

        List<HousekeepingCataloguePage> searchPages = null;

        if (whitelistColumns.contains(field)) {
            searchPages = HousekeepingCatalogueDao.searchCataloguePage(type, field, input);
        } else {
            searchPages = new ArrayList<>();
        }

        if (!(searchPages.size() > 0)) {
            tpl.set("noResults", true);
        }

        tpl.set("searchPages", searchPages);
        tpl.render();
    }

    public static void deletePage (WebConnection client, PlayerDetails playerDetails) {
        int pageId = client.get().getInt("delete");

        if (!StringUtils.isNumeric(client.get().getString("delete")) && client.get().getInt("delete") < 1) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid catalogue page ID");
            client.redirect(getManagePagesPath());
            return;
        }

        HousekeepingCatalogueDao.deletePage(pageId);
        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Deleted Catalogue page with the ID " + pageId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The catalogue page has been successfully deleted");
        client.redirect(getManagePagesPath());

        refreshCataloguePages();
    }

    public static void editPage (WebConnection client, Template tpl) {
        tpl.set("pageEdit", HousekeepingCatalogueDao.getCataloguePages("edit", client.get().getInt("edit"), 0));
        tpl.set("isPageEdit", true);
    }

    public static void savePage (WebConnection client, PlayerDetails playerDetails) {
        int editId = client.post().getInt("editId");
        int editParentId = client.post().getInt("editParentId");
        int editOrderId = client.post().getInt("editOrderId");
        int editMinRank = client.post().getInt("editMinRank");
        int editIsNavigatable = client.post().getInt("editIsNavigatable");
        int editIsHCOnly = client.post().getInt("editIsHCOnly");
        String editName = client.post().getString("editName");
        int editIcon = client.post().getInt("editIcon");
        int editColour = client.post().getInt("editColour");
        String editLayout = client.post().getString("editLayout");
        String editImages = client.post().getString("editImages");
        String editTexts = client.post().getString("editTexts");

        int originalPageId = client.get().getInt("edit");

        if (!StringUtils.isNumeric(client.post().getString("editId")) && editId < 0 ||
                !StringUtils.isNumeric(client.post().getString("editOrderId")) ||
                !StringUtils.isNumeric(client.post().getString("editMinRank")) ||
                !StringUtils.isNumeric(client.post().getString("editIsNavigatable")) ||
                !StringUtils.isNumeric(client.post().getString("editIsHCOnly")) ||
                !StringUtils.isNumeric(client.post().getString("editIcon")) ||
                !StringUtils.isNumeric(client.post().getString("editColour")) ||
                !editTexts.startsWith("[") && !editTexts.endsWith("]") ||
                !editImages.startsWith("[") && !editImages.endsWith("]")) {

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid catalogue values");
            client.redirect(getManagePagesPath() + "?edit=" + editId);
            return;
        }

        HousekeepingCataloguePage cataloguePage = new HousekeepingCataloguePage();
        cataloguePage.setId(editId);
        cataloguePage.setParentId(editParentId);
        cataloguePage.setOrderId(editOrderId);
        cataloguePage.setMinRoleId(editMinRank);
        cataloguePage.setIsNavigatable(editIsNavigatable);
        cataloguePage.setIsClubOnly(editIsHCOnly);
        cataloguePage.setName(editName);
        cataloguePage.setIcon(editIcon);
        cataloguePage.setColour(editColour);
        cataloguePage.setLayout(editLayout);
        cataloguePage.setImages(editImages);
        cataloguePage.setTexts(editTexts);

        HousekeepingCatalogueDao.updatePage(cataloguePage, originalPageId);
        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Edited Catalogue page with the ID " + editId + "(id original: " + originalPageId + "). URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The catalogue page has been successfully saved and updated");
        client.redirect(getManagePagesPath() + "?edit=" + editId);

        refreshCataloguePages();
    }

    public static void copyPage (WebConnection client, PlayerDetails playerDetails) {
        int pageId = client.get().getInt("copy");

        if (!StringUtils.isNumeric(client.get().getString("copy")) && client.get().getInt("copy") < 0) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid catalogue page ID");
            client.redirect(getManagePagesPath());
            return;
        }

        HousekeepingCatalogueDao.copyPage(pageId);
        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Copied Catalogue page with the ID " + pageId + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The catalogue page has been successfully copied, saved and updated");
        client.redirect(getManagePagesPath());

        refreshCataloguePages();
    }

    public static void pagesCreate (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/campaign_management/catalogue_pages_create");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "catalogue/edit_frontpage")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        if (client.post().contains("createParentId")) {
            createPage(client, playerDetails);
            return;
        }

        tpl.set("pageName", "Create catalogue pages");
        tpl.set("allRanks", HousekeepingRankDao.getAllRanksVars());
        tpl.set("ParentNames", HousekeepingCatalogueDao.getAllParentNames());
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getCreatePagesPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/campaign_management/catalogue/pages/create";
    }

    public static void createPage (WebConnection client, PlayerDetails playerDetails) {
        String createParentId = client.post().getString("createParentId");
        String createOrderId = client.post().getString("createOrderId");
        String createMinRank = client.post().getString("createMinRank");
        String createIsNavigatable = client.post().getString("createIsNavigatable");
        String createIsHCOnly = client.post().getString("createIsHCOnly");
        String createName = client.post().getString("createName");
        String createIcon = client.post().getString("createIcon");
        String createColour = client.post().getString("createColour");
        String createLayout = client.post().getString("createLayout");
        String createImages = client.post().getString("createImages");
        String createTexts = client.post().getString("createTexts");

        if (!StringUtils.isNumeric(client.post().getString("createOrderId")) ||
                !StringUtils.isNumeric(client.post().getString("createMinRank")) ||
                !StringUtils.isNumeric(client.post().getString("createIsNavigatable")) ||
                !StringUtils.isNumeric(client.post().getString("createIsHCOnly")) ||
                !StringUtils.isNumeric(client.post().getString("createIcon")) ||
                !StringUtils.isNumeric(client.post().getString("createColour")) ||
                !createTexts.startsWith("[") && !createTexts.endsWith("]") ||
                !createImages.startsWith("[") && !createImages.endsWith("]")) {

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid catalogue values");
            client.redirect(getCreatePagesPath());
            return;
        }

        HousekeepingCatalogueDao.createCataloguePage(createParentId, createOrderId, createMinRank, createIsNavigatable, createIsHCOnly, createName, createIcon, createColour, createLayout, createImages, createTexts);
        HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.STAFF_ACTION, playerDetails.getId(), playerDetails.getName(), "Created Catalogue page with the name " + createName + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The catalogue page has been successfully created and updated");
        client.redirect(getCreatePagesPath());

        refreshCataloguePages();
    }
}
