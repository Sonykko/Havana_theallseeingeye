package org.alexdev.havana.messages.incoming.bots;

import org.alexdev.havana.game.bot.BotManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_GET_BOT_GUIDE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null || room.isPublicRoom()) {
            return;
        }

        if (room.getTaskManager().hasTask("BotGuideCommandTask")) {
            player.send(new ALERT("¡Ya hay un Bot Guía en tu Sala!"));
            return; // Don't duplicate guidebot
        }

        BotManager.getInstance().addGuideBot(room);
    }
}
