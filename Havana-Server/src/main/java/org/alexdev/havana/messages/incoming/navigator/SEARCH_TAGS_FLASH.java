/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.navigator.SEARCH_TAGS_FLASH_LIST;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class SEARCH_TAGS_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new SEARCH_TAGS_FLASH_LIST());
    }
}
