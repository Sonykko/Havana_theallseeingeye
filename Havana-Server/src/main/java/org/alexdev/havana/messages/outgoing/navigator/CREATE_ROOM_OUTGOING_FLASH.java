/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class CREATE_ROOM_OUTGOING_FLASH extends MessageComposer {

    private int roomId;
    private String roomName;

    public CREATE_ROOM_OUTGOING_FLASH(int roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(roomId);
        response.writeString(roomName);
    }

    @Override
    public short getHeader() {
        return 59;
    }
}
