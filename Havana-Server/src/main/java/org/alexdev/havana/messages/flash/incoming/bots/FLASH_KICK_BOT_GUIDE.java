package org.alexdev.havana.messages.flash.incoming.bots;

import org.alexdev.havana.game.bot.BotManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class FLASH_KICK_BOT_GUIDE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Room room = player.getRoomUser().getRoom();

        if (room == null || room.isPublicRoom()) {
            return;
        }

        if (!room.getTaskManager().hasTask("BotGuideCommandTask")) {
            return;
        }

        BotManager.getInstance().removeGuideBots(room);
    }
}
