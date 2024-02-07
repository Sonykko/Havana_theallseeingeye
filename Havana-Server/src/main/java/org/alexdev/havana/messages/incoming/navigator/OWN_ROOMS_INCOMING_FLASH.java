/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.ROOMS_LIST_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.List;

public class OWN_ROOMS_INCOMING_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        List<Room> roomList = RoomManager.getInstance().getRoomsByMode(5,player);

        RoomManager.getInstance().sortRooms(roomList);
        RoomManager.getInstance().ratingSantiyCheck(roomList);

        player.send(new ROOMS_LIST_FLASH(player, roomList, -3));
    }
}
