package org.alexdev.havana.game.bot.enums;

import org.alexdev.havana.dao.mysql.BotGuideDao;
import org.alexdev.havana.game.bot.BotGuide;

public enum BotGuideSpeechDefaultType {
    WELCOME(BotGuideDao.getSpeechBySpeechKey("WELCOME")),
    INTRODUCE(BotGuideDao.getSpeechBySpeechKey("INTRODUCE"));

    private final BotGuide botGuide;

    BotGuideSpeechDefaultType(BotGuide botGuide) {
        this.botGuide = botGuide;
    }

    public BotGuide getBotGuide() {
        return botGuide;
    }
}