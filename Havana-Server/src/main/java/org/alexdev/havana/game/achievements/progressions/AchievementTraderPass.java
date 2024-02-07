package org.alexdev.havana.game.achievements.progressions;

import org.alexdev.havana.game.achievements.AchievementInfo;
import org.alexdev.havana.game.achievements.AchievementProgress;
import org.alexdev.havana.game.achievements.user.UserAchievement;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.util.config.GameConfiguration;

public class AchievementTraderPass implements AchievementProgress {
    @Override
    public boolean tryProgress(Player player, UserAchievement userAchievement, AchievementInfo achievementInfo) {
        return false;
    }

    public static boolean isActivated(String activationCode) {
        return false;
    }
}
