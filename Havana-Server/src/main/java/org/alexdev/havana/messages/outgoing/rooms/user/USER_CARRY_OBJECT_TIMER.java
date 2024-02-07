package org.alexdev.havana.messages.outgoing.rooms.user;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class USER_CARRY_OBJECT_TIMER extends MessageComposer {
    private final int userId;
    private final int timer;

    public USER_CARRY_OBJECT_TIMER(int userId, int timer) {
        this.userId = userId;
        this.timer = timer;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.userId);
        response.writeInt(this.timer);
    }

    @Override
    public short getHeader() {
        return 482;
    }
}
