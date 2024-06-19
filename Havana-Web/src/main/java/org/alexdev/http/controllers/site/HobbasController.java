package org.alexdev.http.controllers.site;

import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.http.dao.HobbasDao;

public class HobbasController {
    public static void hobbas(WebConnection client) {

        Template tpl = client.template("hobbas/apply");

        //PlayerDetails playerDetails = (PlayerDetails) tpl.get("playerDetails");

        if (client.post().queries().size() > 3) {
            String habboname = client.post().getString("habboname");
            String email = client.post().getString("email");
            String firstname = client.post().getString("firstname");
            String lastname = client.post().getString("lastname");

            if (email.matches("^[a-z0-9_.-]+@([a-z0-9]+([-]+[a-z0-9]+)*\\.)+[a-z]{2,7}$")) {
                HobbasDao.insertApplyForm(habboname, email, firstname, lastname);

                client.session().set("alertColour", "success");
                client.session().set("alertMessage", "<text data-translate=\"ALERT_SUCCESS\"></text>");
                client.redirect("/habbo/es/help/contact/hobba/");

                return;
            }

            client.session().set("alertColour", "danger");
            client.session().set("alertMessage", "<text data-translate=\"ALERT_DANGER\"></text>");
        }

        client.session().set("page", "hobba");
        tpl.set("formLang", GameConfiguration.getInstance().getString("hobba.form.lang"));
        tpl.render();

        client.session().delete("alertMessage");
    }
}
