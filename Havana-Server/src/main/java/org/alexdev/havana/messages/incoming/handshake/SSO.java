package org.alexdev.havana.messages.incoming.handshake;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;
import org.alexdev.havana.server.util.MalformedPacketException;

public class SSO implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader){
        if (player.isLoggedIn()) {
            return;
        }

        String ticket = null;
        try {
            ticket = reader.readString();
        } catch (MalformedPacketException e) {
            e.printStackTrace();
        }

        /*
        if (GameConfiguration.getInstance().getBoolean("bot.connection.allow")) {
            if (ticket.startsWith(GameConfiguration.getInstance().getString("bot.connection.sso.prefix"))) {
                String sex = ThreadLocalRandom.current().nextBoolean() ? "M" : "F";
                int userId = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
                String name = ticket.replace(GameConfiguration.getInstance().getString("bot.connection.sso.prefix") + "-", "");

                try {
                    player.getDetails().fill(userId, name, FigureUtil.getRandomFigure(sex, ThreadLocalRandom.current().nextBoolean()), "I'm here to test things up!", sex);
                    player.getDetails().setRank(PlayerRank.NORMAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.login();
                return;
            }
        }
        14bf970f-7f56-4f90-9fc6-6c0070d5bcb8
         */

        if (!PlayerDao.loginTicket(player, ticket)) {
            //player.send(new LOCALISED_ERROR("Incorrect SSO ticket"));
            player.kickFromServer();
        } else {
            player.login();
        }
    }
}
