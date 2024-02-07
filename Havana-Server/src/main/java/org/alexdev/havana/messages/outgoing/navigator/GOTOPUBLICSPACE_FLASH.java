/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class GOTOPUBLICSPACE_FLASH extends MessageComposer {

    private Room room;

    public GOTOPUBLICSPACE_FLASH(Room room) {
        this.room = room;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(room.getId());
        response.writeString(room.getData().getCcts());
        response.writeInt(room.getId());
    }

    @Override
    public short getHeader() {
        return 453;
    }
}
