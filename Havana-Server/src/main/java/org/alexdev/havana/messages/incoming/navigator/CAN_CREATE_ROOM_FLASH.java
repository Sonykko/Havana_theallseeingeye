/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.navigator.SEND_ROOM_CREATE_ERROR_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class CAN_CREATE_ROOM_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        player.send(new SEND_ROOM_CREATE_ERROR_FLASH());
    }
}
