package org.alexdev.http.controllers.habblet;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.dao.ContentModerationDao;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContentModerationController {

    public static void mod_localizations(WebConnection client) {
        String lang = GameConfiguration.getInstance().getString("mod.localizations.lang");

        Template tpl = client.template("habblet/mod/localizations_" + lang);
        tpl.render();
    }

    private static final ConcurrentHashMap<String, Integer> reportCounts = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void add_report(WebConnection client) {
        String uri = client.request().uri();
        String type = uri.replace("/mod/add_", "").replace("_report", "");
        String objectId = client.post().getString("objectId");

        String userIp = client.getIpAddress();

        int count = reportCounts.getOrDefault(userIp, 0);
        int delay = 30;

        if (count >= 10) {
            client.send("SPAM");
            return;
        }

        reportCounts.put(userIp, count + 1);

        if (count + 1 == 10) {
            scheduler.schedule(() -> {
                reportCounts.remove(userIp);
            }, delay, TimeUnit.SECONDS);
        }

        if (type.isEmpty() || !StringUtils.isNumeric(objectId)) {
            client.send("ERROR");
            return;
        }

        ContentModerationDao.addReport(type, Integer.parseInt(objectId));

        client.send("SUCCESS");
    }
}