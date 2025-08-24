package org.alexdev.havana.game.commands.registered;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;

public class FollowCommand extends Command {
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

        Player targetUser = PlayerManager.getInstance().getPlayerByName(args[0]);

        if (targetUser == null) {
            player.send(new ALERT("Can't find the user: " + args[0]));
            return;
        }

        if (targetUser.getDetails().getId() == player.getDetails().getId()) {
            player.send(new ALERT("Can't follow yourself"));
            return;
        }

        // If the target is not a friend don't follow
        if (!player.getMessenger().hasFriend(targetUser.getDetails().getId())) {
            player.send(new ALERT("Friend not found"));
            return;
        }

        if (!targetUser.getDetails().doesAllowStalking()) {
            player.send(new ALERT("Your friend has turned off being followed"));
            return;
        }

        var room = targetUser.getRoomUser().getRoom();

        if (room == null) {
            player.send(new ALERT("Your friend is in the hotel view"));
            return;
        }

        if (player.getRoomUser().getRoom() == room) {
            player.send(new ALERT("Your friend already is in the room"));
            return;
        }

        room.forward(player, false);
    }

    @Override
    public String getDescription() {
        return "Go to your friend room";
    }
}