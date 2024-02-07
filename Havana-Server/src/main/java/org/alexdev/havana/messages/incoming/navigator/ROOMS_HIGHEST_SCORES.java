package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.RECOMMENDED_ROOM_LIST_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.List;

public class ROOMS_HIGHEST_SCORES implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var roomList = RoomManager.getInstance().getRoomsByMode(2, player);

        //RoomManager.getInstance().sortRooms(roomList);
        RoomManager.getInstance().ratingSantiyCheck(roomList);

        player.send(new RECOMMENDED_ROOM_LIST_FLASH(player, roomList, -2));
    }
}
