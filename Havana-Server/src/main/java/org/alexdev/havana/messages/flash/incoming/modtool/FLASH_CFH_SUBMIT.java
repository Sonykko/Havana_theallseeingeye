package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.game.moderation.cfh.CallForHelpManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.flash.outgoing.modtool.FLASH_CFH_RESULT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_CFH_SUBMIT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (CallForHelpManager.getInstance().hasPendingCall(player)) {
            player.send(new FLASH_CFH_RESULT(FLASH_CFH_RESULT.CfhErrorCode.ALREADY_HAVE_TICKET));
            return;
        }

        String message = reader.readString();
        int unknown = reader.readInt();
        int category = reader.readInt();
        int reportedUser = reader.readInt();
        Room room = player.getRoomUser().getRoom();
        CallForHelpManager.getInstance().submitCall(player, category, reportedUser, room, message);
        player.send(new FLASH_CFH_RESULT(FLASH_CFH_RESULT.CfhErrorCode.OK));
    }
}
