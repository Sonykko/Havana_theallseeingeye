package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.moderation.cfh.CallForHelp;
import org.alexdev.havana.game.moderation.cfh.CallForHelpManager;
import org.alexdev.havana.game.moderation.cfh.enums.CFHAction;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_CFH_RESOLVED;
import org.alexdev.havana.messages.outgoing.moderation.CRY_REPLY;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_CFH_CLOSE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {

        if (!player.hasFuse(Fuseright.RECEIVE_CALLS_FOR_HELP)) {
            return;
        }

        int result = reader.readInt(); // 1 = useless, 2 = abusive, 3 = resolved
        int unknown = reader.readInt();
        int ticketId = reader.readInt();

        CallForHelp call = CallForHelpManager.getInstance().getCall(ticketId);

        if (call == null) {
            return;
        }

        call.setDeleted(true);

        Player caller = PlayerManager.getInstance().getPlayerById(call.getCaller());

        if (caller == null) {
            return;
        }

        if (result == 1) {
            if (caller.getNetwork().isFlashConnection()) {
                caller.send(new FLASH_CFH_RESOLVED(FLASH_CFH_RESOLVED.StatusCode.INVALID));
            } else {
                caller.send(new CRY_REPLY("INVALID"));
            }

            CallForHelpManager.getInstance().deleteCall(call, CFHAction.REPLY, String.valueOf((FLASH_CFH_RESOLVED.StatusCode.INVALID)));
        }

        if (result == 2) {
            if (caller.getNetwork().isFlashConnection()) {
                caller.send(new FLASH_CFH_RESOLVED(FLASH_CFH_RESOLVED.StatusCode.ABUSIVE));
            } else {
                caller.send(new CRY_REPLY("ABUSIVE"));
            }

            CallForHelpManager.getInstance().deleteCall(call, CFHAction.REPLY, String.valueOf((FLASH_CFH_RESOLVED.StatusCode.ABUSIVE)));
        }

        if (result == 3) {
            if (caller.getNetwork().isFlashConnection()) {
                caller.send(new FLASH_CFH_RESOLVED(FLASH_CFH_RESOLVED.StatusCode.RESOLVED));
            } else {
                caller.send(new CRY_REPLY("OPGELOST"));
            }

            CallForHelpManager.getInstance().deleteCall(call, CFHAction.REPLY, String.valueOf((FLASH_CFH_RESOLVED.StatusCode.RESOLVED)));
            CallForHelpManager.getInstance().sendCfhsToMods();
        }
    }
}