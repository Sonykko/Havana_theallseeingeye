/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.rooms.FLOOR_MAP;
import org.alexdev.havana.messages.outgoing.rooms.HEIGHTMAP_UPDATE;
import org.alexdev.havana.messages.outgoing.rooms.user.HEIGHTMAP_UPDATE_FLASH;
import org.alexdev.havana.messages.outgoing.rooms.user.USER_OBJECTS;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.List;

public class GET_FLOORMAP_FLASH extends GET_FLOORMAP {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        player.send(new HEIGHTMAP_UPDATE_FLASH(player.getRoomUser().getRoom(), player.getRoomUser().getRoom().getModel()));
        player.send(new FLOOR_MAP(player.getRoomUser().getRoom().getModel()));
        player.send(new USER_OBJECTS(List.of(), player.flash));
    }
}
