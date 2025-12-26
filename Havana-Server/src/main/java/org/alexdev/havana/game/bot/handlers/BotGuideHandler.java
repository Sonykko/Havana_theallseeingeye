package org.alexdev.havana.game.bot.handlers;

import org.alexdev.havana.dao.mysql.BotGuideDao;
import org.alexdev.havana.game.bot.Bot;
import org.alexdev.havana.game.bot.BotGuide;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotGuideHandler {
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
                    String[] responses = speech.getResponse().split("\\|");

                    for (String response : responses) {
                        bot.getRoomUser().talk(response.trim(), CHAT_MESSAGE.ChatMessageType.SHOUT);
                    }

                    if (speech.getSpeechKey().equals("WAVE") || speech.getSpeechKey().equals("DANCE")) {
                        handleAction(bot, speech.getSpeechKey());
                    }
                    break;
                }
            }
        }
    }

    public static void handleAction(Bot bot, String action) {
        if (action.equalsIgnoreCase("WAVE")) {
            bot.getRoomUser().wave();
        }


        if (action.equalsIgnoreCase("DANCE")) {
            bot.getRoomUser().dance(1);
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                bot.getRoomUser().dance(0);
                scheduler.shutdown();
            }, 3, TimeUnit.SECONDS);
        }
    }
}
