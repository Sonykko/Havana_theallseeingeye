package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.dao.mysql.ModerationDao;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.moderation.ModerationActionType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.texts.TextsManager;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.outgoing.moderation.MODERATOR_ALERT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_MODTOOL_USER_KICK implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.KICK)) {
            return;
        }

        int userId = reader.readInt();
        String message = reader.readString();
        String notes = "FLASH_MODTOOL";

        Player target = PlayerManager.getInstance().getPlayerById(userId);

        if (target == null) {
            player.send(new ALERT("Target user is not online."));
        }

        if (target.getDetails().getId() == player.getDetails().getId()) {
            return; // Can't kick yourself!
        }

        if (target.hasFuse(Fuseright.KICK)) {
            player.send(new ALERT(TextsManager.getInstance().getValue("modtool_rankerror")));
            return;
        }

        target.getRoomUser().kick(false, true);
        target.send(new MODERATOR_ALERT(message));

        ModerationDao.addLog(ModerationActionType.KICK_USER, player.getDetails().getId(), target.getDetails().getId(), message, notes);
    }
}