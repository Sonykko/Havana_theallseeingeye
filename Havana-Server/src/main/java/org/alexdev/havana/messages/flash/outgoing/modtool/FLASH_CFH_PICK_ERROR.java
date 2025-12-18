package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class FLASH_CFH_PICK_ERROR extends MessageComposer {
    @Override
    public void compose(NettyResponse response) {
    }

    @Override
    public short getHeader() {
        return 532;
    }
}
