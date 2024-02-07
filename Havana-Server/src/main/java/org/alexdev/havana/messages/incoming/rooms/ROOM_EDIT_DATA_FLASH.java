/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_DATA_OUT_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class ROOM_EDIT_DATA_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var room = player.getRoomUser().getRoom();


        player.send(new ROOM_EDIT_DATA_OUT_FLASH(room));
    }
}
