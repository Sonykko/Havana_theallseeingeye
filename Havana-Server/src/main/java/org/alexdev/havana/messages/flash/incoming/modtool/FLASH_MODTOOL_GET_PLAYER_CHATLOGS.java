package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_MODTOOL_CHATLOG_RESPONSE;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_MODTOOL_GET_PLAYER_CHATLOGS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.CHAT_LOG)) {
            return;
        }

        int userId = reader.readInt();
        PlayerDetails playerDetails = PlayerManager.getInstance().getPlayerData(userId);
        if (playerDetails == null) {
            return;
        }
        player.send(new FLASH_MODTOOL_CHATLOG_RESPONSE(playerDetails));
    }
}
