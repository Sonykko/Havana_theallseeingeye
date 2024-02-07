/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.incoming.rooms.user.CARRYDRINK;
import org.alexdev.havana.messages.outgoing.rooms.*;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_BADGES;
import org.alexdev.havana.messages.outgoing.rooms.user.USER_OBJECTS;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.ArrayList;

public class ROOM_LOAD_FLASH extends G_USRS {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        new G_USRS().handle(player, reader);
        //new G_OBJS().handle(player, reader);
        new G_ITEMS().handle(player, reader);
        new G_STAT().handle(player, reader);

        //player.send(new GXI_PRIVATE_FLASH(player.getDetails().getId(), room));
    }
}
