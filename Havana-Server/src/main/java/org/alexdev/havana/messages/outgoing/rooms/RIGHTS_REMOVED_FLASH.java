/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class RIGHTS_REMOVED_FLASH extends MessageComposer {

    private final int roomId;
    private final int userId;

    public RIGHTS_REMOVED_FLASH(int roomId, int userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(roomId);
        response.writeInt(userId);
    }

    @Override
    public short getHeader() {
        return 511;
    }
}
