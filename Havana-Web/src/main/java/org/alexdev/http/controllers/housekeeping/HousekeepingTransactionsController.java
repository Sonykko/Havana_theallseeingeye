package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.TransactionDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingLogsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.game.housekeeping.enums.HousekeepingLogType;
import org.alexdev.http.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;

public class HousekeepingTransactionsController {

    /**
     * Handle the /housekeeping/users/search URI request
     *
     * @param client the connection
     */
    public static void search(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/transaction_lookup");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "transaction/lookup")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            if (client.post().getValues().size() > 0) {
                var transactions = TransactionDao.getTransactionsPastMonth(client.post().getString("searchQuery"), true);
                tpl.set("transactions", transactions);
            } else {
                tpl.set("noResults", true);
            }

            if (client.get().getValues().size() > 0) {
                var transactions = TransactionDao.getTransactionsPastMonth(client.get().getString("searchQuery"), true);
                tpl.set("transactions", transactions);
            } else {
                tpl.set("noResults", true);
            }
        } catch (Exception ex) {

        }

        tpl.set("pageName", "Transaction Lookup");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }

    public static void item_lookup(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/admin_tools/transaction_item_lookup");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "transaction/lookup")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            HousekeepingLogsDao.logHousekeepingAction(HousekeepingLogType.BAD_PERMISSIONS, playerDetails.getId(), playerDetails.getName(), "URL: " + client.request().uri(), client.getIpAddress());
            return;
        }

        try {
            var transactions = TransactionDao.getTransactionByItem(StringUtils.isNumeric(client.get().getString("id")) ? client.get().getInt("id") : 0);

            if (!(transactions.size() > 0)) {
                tpl.set("noResults", true);
            }
        } catch (Exception e) {

        }

        tpl.set("pageName", "Transaction Lookup");
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}
