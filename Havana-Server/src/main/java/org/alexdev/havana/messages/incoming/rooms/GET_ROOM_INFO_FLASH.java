/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.rooms.SET_ROOM_INFO_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class GET_ROOM_INFO_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var roomId = reader.readInt();
        var unk = reader.readBoolean();
        var unk2 = reader.readBoolean();

        var room = RoomManager.getInstance().getRoomById(roomId);

        if(room == null) {
            System.out.println("COULD NOT FIND ROOM");
            return;
        }

        player.send(new SET_ROOM_INFO_FLASH(room, unk, unk2));
    }
}
