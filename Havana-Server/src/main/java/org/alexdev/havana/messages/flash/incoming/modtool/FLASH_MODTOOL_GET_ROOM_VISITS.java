package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_MODTOOLS_ROOM_VISITS_RESPONSE;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_MODTOOL_GET_ROOM_VISITS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
            return;
        }
        int userId = reader.readInt();

        player.send(new FLASH_MODTOOLS_ROOM_VISITS_RESPONSE(userId));
    }
}
