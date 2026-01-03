package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_MODTOOL_USER_INFO;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_MODTOOL_GET_USER_INFO implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
            return;
        }

        int userId = reader.readInt();

        PlayerDetails target = PlayerManager.getInstance().getPlayerData(userId);
        if (target == null) {
            player.send(new ALERT("Target not found"));
        } else {
            player.send(new FLASH_MODTOOL_USER_INFO(target));
        }

    }
}
