/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.user;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.rooms.user.SEND_WARDROBE_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class GET_WARDROBE_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        boolean hasFuse = player.hasFuse(Fuseright.USE_WARDROBE);
        player.send(new SEND_WARDROBE_FLASH(player, hasFuse));
    }
}
