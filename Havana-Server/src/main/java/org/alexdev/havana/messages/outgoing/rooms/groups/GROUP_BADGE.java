package org.alexdev.havana.messages.outgoing.rooms.groups;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class GROUP_BADGE extends MessageComposer {

    private final int id;
    private final String val;

    public GROUP_BADGE(int id, String val) {
        this.id = id;
        this.val = val;
    }


    @Override
    public void compose(NettyResponse response) {
        response.writeInt(1);
        response.writeInt(this.id);
        response.writeString(this.val);
    }

    @Override
    public short getHeader() {
        return 309;
    }
}
