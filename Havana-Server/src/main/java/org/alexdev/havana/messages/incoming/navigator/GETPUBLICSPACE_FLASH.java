/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.GOTOPUBLICSPACE_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class GETPUBLICSPACE_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var id = reader.readInt();

        var room = RoomManager.getInstance().getRoomById(id);

        if(room == null) {
            System.out.println("Could not find room");
            return;
        }

        player.send(new GOTOPUBLICSPACE_FLASH(room));
    }
}
