package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.dao.mysql.ModerationDao;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.moderation.ModerationActionType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.outgoing.moderation.MODERATOR_ALERT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_MODTOOL_USER_ALERT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.ROOM_ALERT)) {
            return;
        }

        int userId = reader.readInt();
        String message = reader.readString();
        String notes = "FLASH_MODTOOL";

        Player target = PlayerManager.getInstance().getPlayerById(userId);

        target.send(new MODERATOR_ALERT(message));

        ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), target.getDetails().getId(), message, notes);
    }
}