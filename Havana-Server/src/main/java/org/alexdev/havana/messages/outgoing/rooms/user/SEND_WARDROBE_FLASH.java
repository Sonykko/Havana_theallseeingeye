/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms.user;

import org.alexdev.havana.dao.mysql.WardrobeDao;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.Wardrobe;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class SEND_WARDROBE_FLASH extends MessageComposer {

    private Player player;
    private boolean hasFuseForWardrobe;

    public SEND_WARDROBE_FLASH(Player player, boolean hasFuseForWardrobe) {
        this.player = player;
        this.hasFuseForWardrobe = hasFuseForWardrobe;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.hasFuseForWardrobe);
        if(this.hasFuseForWardrobe) {
            var wardrobe = WardrobeDao.getWardrobe(this.player.getDetails().getId());

            if(wardrobe == null) {
                response.writeInt(0);
            } else {
                response.writeInt(wardrobe.size());
                for(Wardrobe w : wardrobe) {
                    response.writeInt(w.getSlotId());
                    response.writeString(w.getFigure());
                    response.writeString(w.getSex());
                }
            }
        }
    }

    @Override
    public short getHeader() {
        return 267;
    }
}
