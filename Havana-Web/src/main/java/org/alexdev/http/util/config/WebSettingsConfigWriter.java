package org.alexdev.http.util.config;

import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.havana.util.config.writer.ConfigWriter;
import org.alexdev.havana.util.config.writer.GameConfigWriter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class WebSettingsConfigWriter implements ConfigWriter {
    @Override
    public Map<String, String>  setConfigurationDefaults() {
        Map<String, String> config = new HashMap<>();
        config.put("site.name", "Habbo");
        config.put("site.path", "http://localhost");
        config.put("static.content.path", "http://localhost");
        config.put("site.imaging.path", "http://localhost");

        config.put("hotel.check.online", "true");

        config.put("loader.game.ip", "127.0.0.1");
        config.put("loader.game.port", "12321");

        config.put("loader.mus.ip", "127.0.0.1");
        config.put("loader.mus.port", "12322");

        config.put("loader.dcr", "http://localhost/dcr/v31/habbo.dcr?");
        config.put("loader.external.variables", "http://localhost/dcr/v31/gamedata/external_variables.txt?");
        config.put("loader.external.texts", "http://localhost/dcr/v31/gamedata/external_texts.txt?");

        config.put("loader.dcr.http", "http://localhost/dcr/v31/habbo.dcr?");
        config.put("loader.external.variables.http", "http://localhost/dcr/v31/gamedata/external_variables.txt?");
        config.put("loader.external.texts.http", "http://localhost/dcr/v31/gamedata/external_texts.txt?");

        config.put("loader.flash.port", "12323");
        config.put("loader.flash.base", "http://localhost/r38/");
        config.put("loader.flash.swf", "http://localhost/r38/Habbo.swf?");
        config.put("loader.flash.external.variables", "http://localhost/r38/external_variables.txt?");
        config.put("loader.flash.external.variables.exe", "http://localhost/r38/external_variables_exe.txt?");
        config.put("loader.flash.external.texts", "http://localhost/r38/external_flash_texts.txt?");

        config.put("registration.disabled", "false");
        config.put("collectables.page", "51");

        config.put("group.purchase.cost", "20");
        config.put("group.default.badge", "b0503Xs09114s05013s05015");

        config.put("hot.groups.community.limit", "8");
        config.put("hot.groups.limit", "10");

        config.put("discussions.per.page", "10");
        config.put("discussions.replies.per.page", "10");

        config.put("alerts.gift.message", "A new gift has arrived. This time you received a %item_name%.");
        config.put("homepage.template.file", "index");

        config.put("free.month.hc.registration", "true");

        config.put("max.tags.users", "8");
        config.put("max.tags.groups", "20");

        /*

                    prop.put("mail.smtp.host", GameConfiguration.getInstance().getString("email.smtp.host"));
            prop.put("mail.smtp.port", GameConfiguration.getInstance().getString("email.smtp.port"));
            prop.put("mail.smtp.connectiontimeout", "5000");
            prop.put("mail.smtp.timeout", "5000");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String username = GameConfiguration.getInstance().getString("email.smtp.login.username");
                    String password = GameConfiguration.getInstance().getString("email.smtp.login.password");
                    return new PasswordAuthentication(username, password);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(GameConfiguration.getInstance().getString("email.smtp.from.email"), GameConfiguration.getInstance().getString("email.smtp.from.name")));

         */

        config.put("trade.email.verification", "false");
        config.put("email.smtp.enable", "false");
        config.put("email.static.content.path", "http://localhost");

        config.put("email.smtp.host", "");
        config.put("email.smtp.port", "465");

        config.put("email.smtp.login.username", "");
        config.put("email.smtp.login.password", "");

        config.put("email.smtp.from.email", "");
        config.put("email.smtp.from.name", "");

        config.put("maintenance", "false");

        config.put("rcon.cfh.reply.message", "¡Gracias por tu alerta! Un miembro del equipo staff investigara tu caso y tomara las acciones oportunas.");
        config.put("rcon.kick.message", "Has sido expulsado por un Moderador.");
        config.put("rcon.superban.message", "Has sido baneado por no respetar la Manera Habbo");
        config.put("rcon.delete.stickie.message", "Nota eliminada por no respetar la Manera Habbo");
        config.put("rcon.room.unacceptable.name", "Nombre contra las normas");
        config.put("rcon.room.unacceptable.desc", "Sala contra las normas");

        config.put("hobba.form.lang", "ES");

        config.put("hk.new.style.enabled", "true");
        config.put("hk.trusted.person.enabled", "false");

        config.putAll(new GameConfigWriter().setConfigurationDefaults());
        return config;
    }

    @Override
    public void setConfigurationData(Map<String, String> config, PrintWriter writer) {

    }
}
