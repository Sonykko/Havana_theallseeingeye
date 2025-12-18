package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.moderation.cfh.CallForHelp;
import org.alexdev.havana.game.moderation.cfh.CallForHelpManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_CFH;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_CFH_PICK_ERROR;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_CFH_PICK implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
            return;
        }

        int unknown = reader.readInt();
        int ticketId = reader.readInt();

        CallForHelp call = CallForHelpManager.getInstance().getCall(ticketId);

        if (call == null || !call.isOpen()) {
            player.send(new FLASH_CFH_PICK_ERROR());
            return;
        }

        call.setPickedUpBy(player);
        CallForHelpManager.getInstance().pickUp(call, player);

        for (Player p : PlayerManager.getInstance().getPlayers()) {
            if (p.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
                p.send(new FLASH_CFH(call));
            }
        }
    }
}
