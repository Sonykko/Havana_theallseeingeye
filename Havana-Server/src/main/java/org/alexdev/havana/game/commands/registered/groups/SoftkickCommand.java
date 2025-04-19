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
import org.alexdev.havana.messages.outgoing.rooms.user.HOTEL_VIEW;

public class SoftkickCommand extends Command {
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
            player.send(new ALERT("Cannot softkick a user who has permission to softkick"));
            return;
        }

        var online = PlayerManager.getInstance().getPlayerById(playerDetails.getId());

        if (online != null) {
            if (online.getRoomUser().getRoom() != null) {
                online.getRoomUser().kick(false, true);
                online.send(new HOTEL_VIEW());
            }
        }

        player.send(new ALERT("Player (" + playerDetails.getName() + ") has been softkicked from the room"));

        ModerationDao.addLog(ModerationActionType.KICK_USER, player.getDetails().getId(), online.getDetails().getId(), args[1], "Command softkick");
    }

    @Override
    public void addArguments() {
        arguments.add("player");
    }

    @Override
    public String getDescription() {
        return "Kicks the player without sending an alert";
    }
}