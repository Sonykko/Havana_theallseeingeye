package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class FLASH_CFH_RESULT extends MessageComposer {
    public enum CfhErrorCode {
        OK,
        ALREADY_HAVE_TICKET,
        ABUSIVE_COOLDOWN
    }

    private final CfhErrorCode cfhErrorCode;

    public FLASH_CFH_RESULT(CfhErrorCode cfhErrorCode) {
        this.cfhErrorCode = cfhErrorCode;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(cfhErrorCode.ordinal());
    }

    @Override
    public short getHeader() {
        return 321;
    }
}
