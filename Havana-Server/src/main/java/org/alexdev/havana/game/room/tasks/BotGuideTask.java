package org.alexdev.havana.game.room.tasks;

import org.alexdev.havana.dao.mysql.BotGuideDao;
import org.alexdev.havana.game.bot.Bot;
import org.alexdev.havana.game.bot.BotSpeech;
import org.alexdev.havana.game.bot.enums.BotGuideSpeechDefaultType;
import org.alexdev.havana.game.pathfinder.Position;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.havana.util.DateUtil;
import org.alexdev.havana.util.config.GameConfiguration;

import java.util.concurrent.ThreadLocalRandom;

public class BotGuideTask implements Runnable {
    private final Room room;

    private int MIN_WALK_TIME = 3;
    private int MAX_WALK_TIME = 10;

    private int MIN_SPEAK_TIME = 20;
    private int MAX_SPEAK_TIME = 50;
    private BotSpeech lastSpeech;

    public BotGuideTask(Room room) {
        this.room = room;

        for (Bot bot : this.room.getEntityManager().getEntitiesByClass(Bot.class)) {
            bot.setNextWalkTime(DateUtil.getCurrentTimeSeconds() + ThreadLocalRandom.current().nextInt(MIN_WALK_TIME, MAX_WALK_TIME));
            bot.setNextSpeechTime(DateUtil.getCurrentTimeSeconds() + ThreadLocalRandom.current().nextInt(MIN_SPEAK_TIME, MAX_SPEAK_TIME));
            bot.getRoomUser().talk(BotGuideDao.getSpeechBySpeechKey(String.valueOf(BotGuideSpeechDefaultType.WELCOME)).getResponse(), CHAT_MESSAGE.ChatMessageType.CHAT);
            bot.getRoomUser().talk(BotGuideDao.getSpeechBySpeechKey(String.valueOf(BotGuideSpeechDefaultType.INTRODUCE)).getResponse(), CHAT_MESSAGE.ChatMessageType.CHAT);
        }
    }

    @Override
    public void run() {
        for (Bot bot : this.room.getEntityManager().getEntitiesByClass(Bot.class)) {
            if (DateUtil.getCurrentTimeSeconds() >= getNextRandomBoundTime()) {
                Position newBound = room.getMapping().getRandomWalkableBound(bot);

                if (newBound != null) {
                    bot.getRoomUser().walkTo(newBound.getX(), newBound.getY());
                }
                setNextRandomBoundTime(DateUtil.getCurrentTimeSeconds() + 5);
            }

            if (DateUtil.getCurrentTimeSeconds() > bot.getNextWalkTime()) {
                if (bot.getBotData().getWalkspace().size() > 0) {
                    Position walkDestination = bot.getBotData().getWalkspace().get(ThreadLocalRandom.current().nextInt(0, bot.getBotData().getWalkspace().size()));
                    bot.getRoomUser().walkTo(walkDestination.getX(), walkDestination.getY());
                    bot.setNextWalkTime(DateUtil.getCurrentTimeSeconds() + ThreadLocalRandom.current().nextInt(MIN_WALK_TIME, MAX_WALK_TIME));

                    if (GameConfiguration.getInstance().getBoolean("april.fools")) {
                        bot.getRoomUser().dance(ThreadLocalRandom.current().nextInt(0, 5));
                    }
                }
            }

            if (DateUtil.getCurrentTimeSeconds() > bot.getNextSpeechTime()) {
                if (bot.getBotData().getSpeeches().size() > 0) {
                    BotSpeech speech = bot.getBotData().getSpeeches().get(ThreadLocalRandom.current().nextInt(0, bot.getBotData().getSpeeches().size()));
                    bot.setNextSpeechTime(DateUtil.getCurrentTimeSeconds() + ThreadLocalRandom.current().nextInt(MIN_SPEAK_TIME, MAX_SPEAK_TIME));

                    if (this.lastSpeech != speech) {
                        bot.getRoomUser().talk(speech.getSpeech(), speech.getChatMessageType());
                    }

                    this.lastSpeech = speech;
                }
            }
        }
    }

    private long nextRandomBoundTime;

    public long getNextRandomBoundTime() {
        return nextRandomBoundTime;
    }

    public void setNextRandomBoundTime(long nextRandomBoundTime) {
        this.nextRandomBoundTime = nextRandomBoundTime;
    }
}
