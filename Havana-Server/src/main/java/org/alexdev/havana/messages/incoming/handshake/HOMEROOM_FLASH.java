/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.handshake;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.handshake.SET_HOME_ROOM_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class HOMEROOM_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var roomId = reader.readInt();
        var room = RoomDao.getRoomById(roomId);

        if(roomId != 0) {
            if(room == null || room.getData().getOwnerId() != player.getDetails().getId()) {
                return;
            }
        } else {
            roomId = PlayerDao.getHomeRoom(player.getDetails().getId());
        }

        if(roomId != 0) {
            player.getDetails().setHomeRoom(roomId);
            PlayerDao.saveHomeRoom(player.getDetails().getId(), roomId);
        }

        player.send(new SET_HOME_ROOM_FLASH(roomId));
    }
}
