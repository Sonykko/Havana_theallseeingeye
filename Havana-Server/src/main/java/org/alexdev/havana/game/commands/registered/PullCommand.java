package org.alexdev.havana.game.commands.registered;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.outgoing.rooms.user.CHAT_MESSAGE;

public class PullCommand extends Command {
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

        Player targetUser = PlayerManager.getInstance().getPlayerByName(args[0]);

        if (targetUser == null) {
            player.send(new ALERT("Can't find the user: " + args[0]));
            return;
        }

        if (targetUser.getDetails().getId() == player.getDetails().getId()) {
            player.send(new ALERT("You can't pull yourself"));
            return;
        }

        if (targetUser.getDetails().getRank().getRankId() > PlayerRank.GUIDE.getRankId()) {
            player.send(new ALERT("You can't pull a Staff member"));
            return;
        }

        if (player.getRoomUser().getRoom() != targetUser.getRoomUser().getRoom()) {
            player.send(new ALERT("You can't pull a player that is not in the room"));
            return;
        }

        var pullTile = player.getRoomUser().getPosition().getSquareInFront();
        var pullTiles = player.getRoomUser().getPosition().getCircle(0);
        var secondTile = pullTile.getSquareInFront();

        pullTiles.add(secondTile);

        if (!pullTiles.contains(targetUser.getRoomUser().getPosition())) {
            player.send(new ALERT("You can't pull that way"));
            return;
        }

        var tileWalkable = targetUser.getRoomUser().getRoom().getMapping().getTile(pullTile);

        if (tileWalkable == null) {
            player.send(new ALERT("You can't pull that way"));
            return;
        }
        if (!tileWalkable.hasWalkableFurni(entity)) {
            player.send(new ALERT("You can't pull that way"));
            return;
        }

        targetUser.getRoomUser().walkTo(pullTile.getX(), pullTile.getY());

        String chatMessage = "* pulls " + targetUser.getDetails().getName() + " *";
        player.getRoomUser().talk(chatMessage, CHAT_MESSAGE.ChatMessageType.WHISPER);
    }

    @Override
    public String getDescription() {
        return "Pull a player";
    }
}