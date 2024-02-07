/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.user;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class REMOVE_ITEM_FLASH extends MessageComposer {
    private int id;
    public REMOVE_ITEM_FLASH(int id) {
        this.id = id;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(id);
    }

    @Override
    public short getHeader() {
        return 99;
    }
}
