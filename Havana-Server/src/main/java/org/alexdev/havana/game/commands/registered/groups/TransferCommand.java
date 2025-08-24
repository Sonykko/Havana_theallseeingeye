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

public class TransferCommand extends Command {
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

        if (CommandManager.getInstance().hasPermission(playerDetails, "transfer")) {
            player.send(new ALERT("Cannot transfer a user who has permission to transfer"));
            return;
        }

        var online = PlayerManager.getInstance().getPlayerById(playerDetails.getId());
        var room = player.getRoomUser().getRoom();

        if (online != null) {
            if (room.isPublicRoom()) {
                room.forward(online, false);
            } else {
                room.getEntityManager().enterRoom(online, null);
            }
        }

        player.send(new ALERT("Transferred " + playerDetails.getName() + " to your room"));

        ModerationDao.addLog(ModerationActionType.TRANSFER_USER, player.getDetails().getId(), online.getDetails().getId(), "", "Command transfer");
    }

    @Override
    public void addArguments() {
        arguments.add("player");
    }

    @Override
    public String getDescription() {
        return "Transfer Habbo to your room";
    }
}