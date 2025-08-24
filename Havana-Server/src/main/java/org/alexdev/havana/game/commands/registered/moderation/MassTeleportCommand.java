package org.alexdev.havana.game.commands.registered.moderation;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerRank;

public class MassTeleportCommand extends Command {
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

        var position = player.getRoomUser().getPosition();

        for (Player user : player.getRoomUser().getRoom().getEntityManager().getPlayers()) {
            user.getRoomUser().warp(position, true, true);
        }
    }

    @Override
    public String getDescription() {
        return "Warp all players of the room";
    }
}