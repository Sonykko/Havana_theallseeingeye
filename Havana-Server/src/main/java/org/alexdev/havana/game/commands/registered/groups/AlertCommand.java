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

public class AlertCommand extends Command {
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

        if (CommandManager.getInstance().hasPermission(playerDetails, "alert")) {
            player.send(new ALERT("Cannot alert a user who has permission to alert"));
            return;
        }

        var online = PlayerManager.getInstance().getPlayerById(playerDetails.getId());

        if (online != null) {
            online.send(new ALERT(args[1]));
        }

        player.send(new ALERT("Gave alert to " + playerDetails.getName()));

        ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getDetails().getId(), online.getDetails().getId(), args[1], "Command alert");
    }

    @Override
    public void addArguments() {
        arguments.add("player");
        arguments.add("message");
    }

    @Override
    public String getDescription() {
        return "Send a alert to the player";
    }
}
