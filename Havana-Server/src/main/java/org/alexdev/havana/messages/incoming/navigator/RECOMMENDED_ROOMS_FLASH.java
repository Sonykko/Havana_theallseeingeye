/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.RECOMMENDED_ROOM_LIST;
import org.alexdev.havana.messages.outgoing.navigator.RECOMMENDED_ROOM_LIST_FLASH;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.List;

public class RECOMMENDED_ROOMS_FLASH extends RECOMMENDED_ROOMS {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var categoryId = -1;

        try {
            categoryId = Integer.parseInt(reader.readString());
        } catch (Exception e) {
            //Do nothing, it's not searched by category
        }

        List<Room> roomList;

        if(categoryId == -1) {
            roomList = RoomManager.getInstance().getRoomsByMode(1,player);
        } else {
            roomList = RoomManager.getInstance().getRoomsByCategory(categoryId);
        }

        RoomManager.getInstance().sortRooms(roomList);
        RoomManager.getInstance().ratingSantiyCheck(roomList);

        player.send(new RECOMMENDED_ROOM_LIST_FLASH(player, roomList, categoryId));
    }
}
