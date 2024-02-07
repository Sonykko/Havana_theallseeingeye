/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.handshake;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class UNKNOWN_290_FLASH extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
        response.writeBool(true);
        response.writeBool(false);
    }

    @Override
    public short getHeader() {
        return 290;
    }
}
