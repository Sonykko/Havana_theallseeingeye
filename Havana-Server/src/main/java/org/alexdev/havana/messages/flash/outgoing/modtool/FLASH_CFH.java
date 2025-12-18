package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.game.moderation.cfh.CallForHelp;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class FLASH_CFH extends MessageComposer {
    private final CallForHelp cfh;

    public FLASH_CFH(CallForHelp cfh) {
        this.cfh = cfh;
    }

    @Override
    public void compose(NettyResponse response) {
        int ticketStatus = 0;
        if (cfh.isOpen()) {
            ticketStatus = 1;
        } else if (!cfh.isOpen() && !cfh.isDeleted()) {
            ticketStatus = 2;
        }
        response.writeInt(this.cfh.getCryId());
        response.writeInt(ticketStatus); // 1 = open, 2 = picked, 0 = ??
        response.writeInt(11);
        response.writeInt(0); // categoriy id
        response.writeInt(11);
        response.writeInt(0); // score
        response.writeInt(this.cfh.getCaller()); // Player id
        response.writeString(this.cfh.getCaller()); // player name
        response.writeInt(this.cfh.getReportedUser()); // Target
        response.writeString(cfh.getReportedUser() > 0 ? PlayerManager.getInstance().getPlayerData(cfh.getReportedUser()).getName() : ""); // target name
        response.writeInt(cfh.getPickedUpBy()); // Mod ID
        response.writeString(cfh.getPickedUpBy() > 0 ? PlayerManager.getInstance().getPlayerData(cfh.getPickedUpBy()).getName() : ""); // mod name
        response.writeString(this.cfh.getMessage()); // msg
        if (this.cfh.getRoom() != null) {
            response.writeInt(this.cfh.getRoom().getData().getId());
            response.writeString(this.cfh.getRoom().getData().getName());
        }
    }

    @Override
    public short getHeader() {
        return 530; // "BT"
    }
}
