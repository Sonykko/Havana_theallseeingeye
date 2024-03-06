package org.alexdev.http.controllers.housekeeping;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.dao.mysql.SettingsDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.http.Routes;
import org.alexdev.http.dao.housekeeping.HousekeepingSettingsDao;
import org.alexdev.http.game.housekeeping.HousekeepingManager;
import org.alexdev.http.util.SessionUtil;


import java.util.ArrayList;
import java.util.Map;
import java.util.List;


public class HousekeepingConfigController {
    /**
     * Handle the /housekeeping/articles URI request
     *
     * @param client the connection
     */
    public static void configurations(WebConnection client) {
        if (!client.session().getBoolean(SessionUtil.LOGGED_IN_HOUSKEEPING)) {
            client.redirect("/" + Routes.HOUSEKEEPING_DEFAULT_PATH);
            return;
        }

        Template tpl = client.template("housekeeping/system_status/configurations");
        tpl.set("housekeepingManager", HousekeepingManager.getInstance());

        PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (!HousekeepingManager.getInstance().hasPermission(playerDetails.getRank(), "configuration")) {
            client.redirect("/" + Routes.HOUSEKEEPING_PATH + "/permissions");
            return;
        }

        if (client.post().queries().size() > 0) {
            SettingsDao.updateSettings(client.post().getValues().entrySet());
            // Reload config
            // GameConfiguration.getInstance(new WebSettingsConfigWriter());

            client.session().set("alertColour", "success");
            client.session().set("alertMessage", "All configuration values have been saved successfully! It will take effect within 30 seconds.");
        }

        String categoryName = client.get().getString("settings");

        List<Map<String, Object>> settings = HousekeepingSettingsDao.getSettings(categoryName);
        boolean CategoryExists = HousekeepingSettingsDao.CheckCategory(categoryName);

        if (CategoryExists) {
            tpl.set("CategoryExists", true);
        } else {
            tpl.set("CategoryExists", false);
        }

        List<Map<String, Object>> settingDescriptions = HousekeepingSettingsDao.getSettingDescriptions();

        List<Map<String, Object>> settingsWithDescriptions = new ArrayList<>();

        for (Map<String, Object> setting : settings) {
            String settingName = (String) setting.get("setting");

            for (Map<String, Object> description : settingDescriptions) {
                if (settingName.equals(description.get("setting"))) {
                    setting.put("description", description.get("description"));
                    break;
                }
            }

            settingsWithDescriptions.add(setting);
        }

        tpl.set("pageName", "Configurations");
        tpl.set("configs", settingsWithDescriptions);
        tpl.set("categoryName", categoryName);
        tpl.render();

        // Delete alert after it's been rendered
        client.session().delete("alertMessage");
    }
}
