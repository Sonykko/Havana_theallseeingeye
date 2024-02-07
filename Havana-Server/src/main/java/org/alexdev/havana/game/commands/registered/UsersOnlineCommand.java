package org.alexdev.havana.game.commands.registered;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.commands.CommandFormatBuilder;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.util.StringUtil;

import java.util.List;
import java.util.Map;

public class UsersOnlineCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.NORMAL);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        int maxPlayersPerLine = 5;

        List<Player> players = PlayerManager.getInstance().getPlayers();
        Map<Integer, List<Player>> paginatedPlayers = StringUtil.paginate(players, maxPlayersPerLine);

        Player session = (Player) entity;

        CommandFormatBuilder sb = new CommandFormatBuilder(entity)
                .append("Users online: ").append(players.size()).newLine()
                .append("Daily player peak count: ").append(PlayerManager.getInstance().getDailyPlayerPeak()).newLine()
                .append("List of users online: ").newLine().newLine();

        for (List<Player> playerList : paginatedPlayers.values()) {
            int i = 0;
            int length = playerList.size();
            for (Player player : playerList) {
                if (!player.getDetails().isOnlineStatusVisible()) {
                    continue;
                }

                sb.append(player.getDetails().getName());

                i++;

                if (i < length) {
                    sb.append(", ");
                }
            }

            sb.newLine();
        }

        session.send(new ALERT(sb.toString()));
    }

    @Override
    public String getDescription() {
        return "Get the list of players currently online";
    }
}
