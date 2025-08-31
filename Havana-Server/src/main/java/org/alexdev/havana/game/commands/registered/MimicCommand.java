package org.alexdev.havana.game.commands.registered;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.misc.figure.FigureManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;

public class MimicCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.NORMAL);
    }

    @Override
    public void addArguments() {
        this.arguments.add("player");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        if (!player.getDetails().hasClubSubscription() && player.getDetails().getRank().getRankId() < PlayerRank.MODERATOR.getRankId()) {
            player.send(new ALERT("You need to be a Habbo Club member to use this command"));
            return;
        }

        PlayerDetails targetUser = PlayerDao.getDetails(args[0]);

        if (targetUser == null) {
            player.send(new ALERT("Can't find the user: " + args[0]));
            return;
        }

        if (targetUser.getId() == player.getDetails().getId()) {
            player.send(new ALERT("You can't copy your own look"));
            return;
        }

        if (targetUser.getRank().getRankId() > PlayerRank.GUIDE.getRankId()) {
            player.send(new ALERT("You can't copy the look of a Staff member"));
        }

        var lookToCopy = targetUser.getFigure();
        var genderToCopy = targetUser.getSex();

        if (lookToCopy == null || genderToCopy == null) {
            return;
        }

        if(!FigureManager.getInstance().validateFigure(lookToCopy, genderToCopy, player.getDetails().hasClubSubscription())) {
            return;
        }

        player.getDetails().setFigure(lookToCopy);
        player.getDetails().setSex(genderToCopy);
        PlayerDao.saveDetails(player.getDetails().getId(), lookToCopy, player.getDetails().getPoolFigure(), genderToCopy);

        player.getRoomUser().refreshAppearance();
    }

    @Override
    public String getDescription() {
        return "Copy/mimic the look of another user";
    }
}