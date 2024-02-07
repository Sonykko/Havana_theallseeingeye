/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.ROOMS_LIST_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class ROOMS_OWNED_BY_MY_FRIENDS_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var roomList = RoomManager.getInstance().getRoomsByMode(3,player);

        RoomManager.getInstance().sortRooms(roomList);
        RoomManager.getInstance().ratingSantiyCheck(roomList);

        player.send(new ROOMS_LIST_FLASH(player, roomList, -4));
    }
}
