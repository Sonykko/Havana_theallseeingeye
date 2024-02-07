/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.user;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class AVATAR_SAVE_SEND_FLASH extends MessageComposer {

    private Player player;

    public AVATAR_SAVE_SEND_FLASH(Player player) {
        this.player = player;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(-1);
        response.writeString(this.player.getDetails().getFigure());
        response.writeString(this.player.getDetails().getSex().toLowerCase());
        response.writeString(this.player.getDetails().getMotto());
    }

    @Override
    public short getHeader() {
        return 266;
    }
}
