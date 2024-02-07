/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class FAVOURITE_ROOM_EVENT_FLASH extends MessageComposer {

    private int id;
    private boolean added;

    public FAVOURITE_ROOM_EVENT_FLASH(int id, boolean added) {
        this.id = id;
        this.added = added;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(id);
        response.writeBool(added);
    }

    @Override
    public short getHeader() {
        return 459;
    }
}
