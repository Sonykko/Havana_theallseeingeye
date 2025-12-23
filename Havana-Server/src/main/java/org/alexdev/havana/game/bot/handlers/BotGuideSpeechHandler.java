package org.alexdev.havana.game.bot.handlers;

import org.alexdev.havana.dao.mysql.BotGuideDao;
import org.alexdev.havana.game.bot.Bot;
import org.alexdev.havana.game.bot.BotGuide;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;

public class BotGuideSpeechHandler {
    public static void handleSpeech(Room room, String message) {
        if (room == null) {
            return;
        }

        if (!room.getTaskManager().hasTask("BotGuideCommandTask")) {
            return;
        }

        String messageUpper = message.toUpperCase();

        for (Bot bot : room.getEntityManager().getEntitiesByClass(Bot.class)) {
            for (BotGuide speech : BotGuideDao.getAllSpeech()) {
                if (messageUpper.matches(".*\\b" + speech.getSpeechTrigger() + "\\b.*")) {
                    bot.getRoomUser().talk(speech.getResponse(), CHAT_MESSAGE.ChatMessageType.SHOUT);
                    break;
                }
            }
        }
    }
}
