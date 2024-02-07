package org.alexdev.havana.game.achievements.progressions;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.achievements.AchievementInfo;
import org.alexdev.havana.game.achievements.AchievementProgress;
import org.alexdev.havana.game.achievements.user.UserAchievement;
import org.alexdev.havana.game.player.Player;

public class AchievementEmailVerification implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        var discordVerified = player.getDetails().discordVerified;

        if(!discordVerified) {
            discordVerified = PlayerDao.hasDiscordId(player.getDetails().getId());
        }

        if (discordVerified) {
            userAchievement.setProgress(achievementInfo.getProgressRequired());
            return true;
        }

        return false;
    }
}
