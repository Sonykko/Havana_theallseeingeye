package org.alexdev.havana.game.commands.registered.moderation;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.commands.CommandFormatBuilder;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.util.StringUtil;

public class CoordsCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.COMMUNITY_MANAGER);
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

        var coords = new CommandFormatBuilder(entity);

        coords.append("Your coordinates:").newLine();
        coords.append("Room ID: " + player.getRoomUser().getRoom().getId()).newLine();
        coords.append("X: " + player.getRoomUser().getPosition().getX()).newLine();
        coords.append("Y: " + player.getRoomUser().getPosition().getY()).newLine();
        coords.append("Z: " + StringUtil.format(player.getRoomUser().getPosition().getZ())).newLine();
        coords.append("Head rotation: " + player.getRoomUser().getPosition().getHeadRotation()).newLine();
        coords.append("Body rotation: " + player.getRoomUser().getPosition().getBodyRotation()).newLine();

        player.send(new ALERT(coords.toString()));
    }

    @Override
    public String getDescription() {
        return "Shows the coordinates in the room";
    }
}
