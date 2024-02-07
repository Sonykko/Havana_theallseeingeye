/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.user;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class UPDATE_INVENTORY_FLASH extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {

    }

    @Override
    public short getHeader() {
        return 101;
    }
}
