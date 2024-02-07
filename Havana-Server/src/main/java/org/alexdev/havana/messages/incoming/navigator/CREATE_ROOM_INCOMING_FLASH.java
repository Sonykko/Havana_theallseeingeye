/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.dao.mysql.NavigatorDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.CREATE_ROOM_OUTGOING_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class CREATE_ROOM_INCOMING_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var roomName = reader.readString();
        var modelName = reader.readString();
        var roomState = reader.readString(); //unused, should always be open

        var room = NavigatorDao.createRoom(player.getDetails().getId(), roomName, modelName, true, 0);

        if(room != 0) {
            player.send(new CREATE_ROOM_OUTGOING_FLASH(room, roomName));
        }
    }
}
