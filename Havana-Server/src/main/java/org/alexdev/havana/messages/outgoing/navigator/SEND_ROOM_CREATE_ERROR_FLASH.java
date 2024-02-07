/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class SEND_ROOM_CREATE_ERROR_FLASH extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeBool(false); //true = error
        response.writeInt(99999); //error id

        //TODO: Room limit
    }

    @Override
    public short getHeader() {
        return 512;
    }
}
