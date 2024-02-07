/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class GXI_PRIVATE_FLASH extends MessageComposer {
    private int userId;
    private Room room;

    public GXI_PRIVATE_FLASH(int userId, Room room) {
        this.userId = userId;
        this.room = room;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(true); //isPrivate
        response.writeInt(room.getId());
        if(room.isOwner(userId)) {
            response.writeBool(true);
        } else {
            response.writeBool(false);
        }

        //IF public
        //response.writeString(room.getData().getName()); //TODO: unitPropertySet
        //response.writeInt(room.getId()); //TODO: worldId
    }

    @Override
    public short getHeader() {
        return 471;
    }
}
