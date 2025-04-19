package org.alexdev.havana.game.commands.registered.groups;

import org.alexdev.havana.dao.mysql.ModerationDao;
import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.commands.CommandManager;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.moderation.ModerationActionType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;

public class KickCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.MODERATOR);
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

        String name = args[0];

        PlayerDetails playerDetails = PlayerManager.getInstance().getPlayerData(name);

        if (playerDetails == null) {
            player.send(new ALERT("Could not find user: " + name));
            return;
        }

        if (CommandManager.getInstance().hasPermission(playerDetails, "kick")) {
            player.send(new ALERT("Cannot kick a user who has permission to kick"));
            return;
        }

        var online = PlayerManager.getInstance().getPlayerById(playerDetails.getId());

        if (online != null) {
            if (online.getRoomUser().getRoom() != null) {
                online.getRoomUser().kick(true, true);
                online.send(new ALERT(args[1]));
            }
        }

        player.send(new ALERT("Player (" + playerDetails.getName() + ") has been kicked from the room"));

        ModerationDao.addLog(ModerationActionType.KICK_USER, player.getDetails().getId(), online.getDetails().getId(), args[1], "Command kick");
    }

    @Override
    public void addArguments() {
        arguments.add("player");
        arguments.add("message");
    }

    @Override
    public String getDescription() {
        return "Kick the player while simultaneously sending an alert";
    }
}