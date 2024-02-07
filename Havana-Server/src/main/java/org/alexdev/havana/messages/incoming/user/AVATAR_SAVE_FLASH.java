/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.user;

import org.alexdev.havana.dao.mysql.AchievementDao;
import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.WardrobeDao;
import org.alexdev.havana.game.achievements.user.UserAchievement;
import org.alexdev.havana.game.misc.figure.FigureManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.Wardrobe;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.rooms.user.FIGURE_CHANGE;
import org.alexdev.havana.messages.outgoing.user.AVATAR_SAVE_SEND_FLASH;
import org.alexdev.havana.messages.outgoing.user.USER_OBJECT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;
import org.alexdev.havana.util.ValidationUtil;

public class AVATAR_SAVE_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var gender = reader.readString().toUpperCase();
        var look = reader.readString();

        if (!FigureManager.getInstance().validateFigure(look, gender, player.getDetails().hasClubSubscription())) {
            return;
        }

        player.getDetails().setFigure(look);
        player.getDetails().setSex(gender);

        PlayerDao.saveDetails(player.getDetails().getId(), look, player.getDetails().getPoolFigure(), gender);

        // Send refresh to user
        player.send(new USER_OBJECT(player.getDetails()));

        // Send refresh to room if inside room
        if (player.getRoomUser().getRoom() != null) {
            player.getRoomUser().getRoom().send(new FIGURE_CHANGE(player.getRoomUser().getInstanceId(), player.getDetails()));
        }
    }
}
     