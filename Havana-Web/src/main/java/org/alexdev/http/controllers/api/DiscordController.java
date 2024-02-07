/**
 * Author: Parsnip#5170
 */

package org.alexdev.http.controllers.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.server.rcon.RconConnectionHandler;
import org.alexdev.havana.server.rcon.messages.RconHeader;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.util.RconUtil;
import org.alexdev.http.util.SessionUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DiscordController {
    public static void verify(WebConnection webConnection) {
        var code = webConnection.get().getString("code");

        try {
            var data = 
                "client_id=" + URLEncoder.encode("CHANGE ME", "UTF-8") + "&" +
                "client_secret=" + URLEncoder.encode("CHANGE ME", "UTF-8") + "&" +
                "grant_type=" + URLEncoder.encode("authorization_code", "UTF-8") + "&" +
                "code=" + URLEncoder.encode(code, "UTF-8") + "&" +
                "redirect_uri=" + URLEncoder.encode(GameConfiguration.getInstance().getString("site.path") + "/api/discord", "UTF-8") + "&" +
                "scope=" + URLEncoder.encode("identify", "UTF-8");

            var url = new URL("https://discord.com/api/oauth2/token");
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(data.length()));
            connection.setDoOutput(true);

            var wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            // Read the response
            var responseCode = connection.getResponseCode();
            var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            var response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            webConnection.send(response.toString());

            var mapper = new ObjectMapper();
            var token = mapper.readTree(response.toString()).get("access_token").asText();

            var url2 = new URL("https://discordapp.com/api/users/@me");
            var connection2 = (HttpURLConnection) url2.openConnection();
            connection2.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode2 = connection2.getResponseCode();

            var in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
            String inputLine2;
            StringBuffer response2 = new StringBuffer();

            while ((inputLine2 = in2.readLine()) != null) {
                response2.append(inputLine2);
            }
            in2.close();

            var id = new BigDecimal(mapper.readTree(response2.toString()).get("id").asText());

            var authenticated = webConnection.session().getBoolean("authenticated");

            if (authenticated) {
                var uid = PlayerDao.getByDiscordId(id);
                if (uid > 0) {
                    webConnection.session().set("alertMessage", "Your Discord account is already linked to another user\n");
                    webConnection.redirect("/");
                    return;
                }
                else
                {
                    uid = webConnection.session().getInt("user.id");
                    PlayerDao.setDiscordId(uid, id);
                    webConnection.session().set("discord.saved.alert", true);
                    webConnection.redirect("/");
                    RconUtil.sendCommand(RconHeader.REFRESH_TRADE_SETTING, new HashMap<>());
                    return;
                }
            }
            else {
                if (SessionUtil.login(webConnection, id, true)) {
                    webConnection.redirect("/security_check");
                }
                else {
                    webConnection.redirect("/");
                }
                return;
            }
        } catch (Exception e) {
            webConnection.session().set("alertMessage", "Something went wrong\n");
            webConnection.redirect("/");
            return;
        }
    }
}