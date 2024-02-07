/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.handshake;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class RoomInfoFeed_517_FLASH extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeBool(true);
    }

    @Override
    public short getHeader() {
        return 517;
    }
}
