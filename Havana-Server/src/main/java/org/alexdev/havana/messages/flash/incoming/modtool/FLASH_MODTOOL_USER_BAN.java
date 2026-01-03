package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.dao.mysql.BanDao;
import org.alexdev.havana.game.ban.BanManager;
import org.alexdev.havana.game.ban.BanType;
import org.alexdev.havana.game.commands.CommandManager;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;
import org.alexdev.havana.util.DateUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FLASH_MODTOOL_USER_BAN implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.BAN)) {
            return;
        }

        int userId = reader.readInt();
        String message = reader.readString();
        int banHours = reader.readInt();
        //String notes = "FLASH_MODTOOL";

        Map<BanType, String> criteria = new HashMap<>();
        PlayerDetails playerDetails = PlayerManager.getInstance().getPlayerData(userId);

        if (playerDetails == null) {
            player.send(new ALERT("Could not find user"));
            return;
        }

        if (playerDetails.getId() == player.getDetails().getId()) {
            player.send(new ALERT("Cannot ban yourself"));
        }

        if (playerDetails.isBanned() != null) {
            player.send(new ALERT("User is already banned!"));
        }

        if (CommandManager.getInstance().hasPermission(playerDetails, "ban"))
            player.send(new ALERT("Cannot ban a user who has permission to ban"));

        long banTime = DateUtil.getCurrentTimeSeconds() + TimeUnit.HOURS.toSeconds(banHours);

        BanDao.addBan(BanType.USER_ID, String.valueOf(playerDetails.getId()), banTime, message, player.getDetails().getId());
        criteria.put(BanType.USER_ID, String.valueOf(playerDetails.getId()));

        Player target = PlayerManager.getInstance().getPlayerById(playerDetails.getId());

        if (target != null) {
            target.getNetwork().disconnect();
        }

        BanManager.getInstance().disconnectBanAccounts(criteria);
        player.send(new ALERT("The user " + playerDetails.getName() + " has been banned."));
    }
}

