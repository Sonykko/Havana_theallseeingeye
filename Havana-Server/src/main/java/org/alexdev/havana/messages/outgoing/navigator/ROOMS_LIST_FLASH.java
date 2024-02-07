/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.List;

public class ROOMS_LIST_FLASH extends MessageComposer {

    private Player player;
    private List<Room> rooms;
    private int mode;

    public ROOMS_LIST_FLASH(Player player, List<Room> rooms, int mode) {
        this.player = player;
        this.rooms = rooms;
        this.mode = mode;
    }

    @Override
    public void compose(NettyResponse response) {
        RECOMMENDED_ROOM_LIST_FLASH.list(player, response, this.mode, rooms);
    }

    @Override
    public short getHeader() {
        return 451;
    }
}
