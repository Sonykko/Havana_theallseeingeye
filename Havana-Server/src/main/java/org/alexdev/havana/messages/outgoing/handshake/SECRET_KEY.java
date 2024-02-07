package org.alexdev.havana.messages.outgoing.handshake;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class SECRET_KEY extends MessageComposer {
    private final String key;
    private boolean isFlash;

    public SECRET_KEY(String key, boolean isFlash) {
        this.key = key;
        this.isFlash = isFlash;
    }

    @Override
    public void compose(NettyResponse response) {
        response.write(this.key);
    }

    @Override
    public short getHeader() {
        return isFlash ? (short)257 : (short)1; // "@A"
    }
}
