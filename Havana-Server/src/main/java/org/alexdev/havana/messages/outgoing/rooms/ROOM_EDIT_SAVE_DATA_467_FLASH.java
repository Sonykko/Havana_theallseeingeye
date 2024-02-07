/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class ROOM_EDIT_SAVE_DATA_467_FLASH extends MessageComposer {
    private int roomId;

    public ROOM_EDIT_SAVE_DATA_467_FLASH(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(roomId);
    }

    @Override
    public short getHeader() {
        return 467;
    }
}
