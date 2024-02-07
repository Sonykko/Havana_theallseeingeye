/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class GXI_FLASH extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(false);
        response.writeInt(1);  //wall thickness?
        response.writeInt(1); //floor thickness?
    }

    @Override
    public short getHeader() {
        return 472;
    }
}
