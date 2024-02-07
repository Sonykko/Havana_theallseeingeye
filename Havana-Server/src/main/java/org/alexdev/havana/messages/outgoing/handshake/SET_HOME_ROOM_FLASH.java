/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.handshake;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class SET_HOME_ROOM_FLASH extends MessageComposer {

    private int homeRoomId;

    public SET_HOME_ROOM_FLASH(int homeRoomId) {
        this.homeRoomId = homeRoomId;
    }

    @Override
    public void compose(NettyResponse response) {

        response.writeInt(homeRoomId);
    }

    @Override
    public short getHeader() {
        return 455;
    }
}
