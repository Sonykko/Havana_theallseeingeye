package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;
import org.alexdev.havana.util.DateUtil;

public class FLASH_MODTOOL_USER_INFO extends MessageComposer {
    private final PlayerDetails target;

    public FLASH_MODTOOL_USER_INFO(PlayerDetails target) {
        this.target = target;
    }

    @Override
    public void compose(NettyResponse response) {
        if (target == null) {
            return;
        }

        response.writeInt(target.getId());
        response.writeString(target.getName());
        response.writeInt((DateUtil.getCurrentTimeSeconds() - (int) target.getJoinDate()) / 60); // registration date
        response.writeInt((DateUtil.getCurrentTimeSeconds() - (int) target.getLastOnline()) / 60); // last online
        response.writeBool(PlayerDao.isPlayerOnline(target.getId()));
        response.writeInt(target.getCFHCount()); // cfhs
        response.writeInt(target.getCFHAbusiveCount()); // abusive cfhs
        response.writeInt(target.getCFHCautionsCount()); // cautions
        response.writeInt(target.getBanCount()); // bans
    }

    @Override
    public short getHeader() {
        return 533;
    }
}
