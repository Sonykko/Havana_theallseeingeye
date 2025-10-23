package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingCoinsDao;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.HousekeepingVouchers;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;

public class HousekeepingCoinsController {
    public static void vouchers (WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/vouchers");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "user/create")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction("BAD_PERMISSIONS", playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        String action = client.post().getString("action");

        if ("createVoucher".equals(action)) {
            createVoucher(client, playerDetails);
            return;
        }

        if ("searchVoucher".equals(action)) {
            searchVoucher(client, tpl);
        }

        if ("deleteVoucher".equals(action)) {
            deleteVoucher(client, playerDetails);
            return;
        }

        tpl.set("pageName", "Vouchers codes");
        tpl.set("Vouchers", HousekeepingCoinsDao.getAllVouchers());
        tpl.set("voucherRandom", generateRandomCode());
        tpl.render();

        client.session().delete("alertMessage");
    }

    private static String getVouchersPath() {
        return "/" + Routes.HOUSEKEEPING_PATH + "/admin_tools/vouchers";
    }
    
    public static void createVoucher (WebConnection client, PlayerDetails playerDetails) {
        String credits = "";
        String expiryDate = "";

        String voucherCode = client.post().getString("voucherCode");
        String item = client.post().getString("item");
        if (StringUtils.isNumeric(client.post().getString("credits")) && client.post().getInt("credits") > 0) {
            credits = client.post().getString("credits");
        } else {
            credits = "0";
        }
        if (!client.post().getString("expiryDate").isEmpty()) {
            expiryDate = client.post().getString("expiryDate");
        } else {
            expiryDate = null;
        }
        boolean isSingleUse = client.post().getBoolean("isSingleUse");
        boolean allowNewUsers = client.post().getBoolean("allowNewUsers");

        if (voucherCode.isEmpty() || item.isEmpty() && credits.equals("0")) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid Voucher or sale code or amount of credits");
            client.redirect(getVouchersPath());
            return;
        }

        //List<Map<String, Object>> checkVoucherCode = HousekeepingCoinsDao.getVoucherByCode(voucherCode);
        var checkVoucherCode = HousekeepingCoinsDao.getVoucherByCode(voucherCode);

        if (checkVoucherCode != null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Voucher code already exists");
            client.redirect(getVouchersPath());
            return;
        }

        String type = StringUtils.isEmpty(item) ? "" : "voucherItem";

        HousekeepingCoinsDao.createVoucher(voucherCode, credits, expiryDate, isSingleUse, allowNewUsers, item, type);
        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Created the voucher code " + voucherCode + " with a value of " + credits + " credits. URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Voucher code has been successfully created");
        client.redirect(getVouchersPath());
    }

    public static void searchVoucher(WebConnection client, Template tpl) {
        String query = client.post().getString("searchStr");

        if (StringUtils.isEmpty(query)) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "Please enter a valid search value");
            client.redirect(getVouchersPath());
            return;
        }

        List<HousekeepingVouchers> searchVouchers = HousekeepingCoinsDao.searchVouchers(query);

        tpl.set("query", query);
        tpl.set("searchVouchersDetails", searchVouchers);
        tpl.set("searchEmpty", searchVouchers.isEmpty());
    }

    public static void deleteVoucher(WebConnection client, PlayerDetails playerDetails) {
        String voucherCode = client.post().getString("voucherCode");

        var voucher = HousekeepingCoinsDao.getVoucherByCode(voucherCode);

        if (voucher == null) {
            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "The Voucher code not exists");
            client.redirect(getVouchersPath());
            return;
        }

        HousekeepingCoinsDao.deleteVoucher(voucher.getVoucherCode());

        if (voucher.getSaleCode() != null) {
            HousekeepingCoinsDao.deleteVoucherItem(voucher.getVoucherCode());
        }

        HousekeepingLogsDao.logHousekeepingAction("STAFF_ACTION", playerDetails.getId(), playerDetails.getName(), "Deleted the voucher code " + voucherCode + ". URL: " + client.request().uri(), client.getIpAddress());

        client.session().set("alertColour", "success");
        client.session().set("alertMessage", "The Voucher code has been successfully deleted");
        client.redirect(getVouchersPath());
    }

    private static String generateRandomCode() {
        int codeLength = 8;
        StringBuilder randomCode = new StringBuilder();

        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());

            char randomChar = characters.charAt(index);

            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }
}
