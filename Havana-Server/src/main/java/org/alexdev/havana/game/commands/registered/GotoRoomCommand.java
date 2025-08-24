package org.alexdev.havana.game.commands.registered;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.apache.commons.lang3.math.NumberUtils;

public class GotoRoomCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.NORMAL);
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

        int roomId = NumberUtils.isParsable(args[0]) ? Integer.parseInt(args[0]) : 0;
        var room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            player.send(new ALERT("Room not found"));
            return;
        }

        room.forward(player, false);
    }

    @Override
    public void addArguments() {
        arguments.add("room(ID)");
    }

    @Override
    public String getDescription() {
        return "Teleport yourself to selected room";
    }
}