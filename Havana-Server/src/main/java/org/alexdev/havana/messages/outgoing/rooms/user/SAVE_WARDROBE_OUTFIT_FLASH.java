/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms.user;

import org.alexdev.havana.dao.mysql.WardrobeDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;
import org.alexdev.havana.util.ValidationUtil;

public class SAVE_WARDROBE_OUTFIT_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var slotId = reader.readInt();
        var figure = reader.readString();
        var gender = reader.readString();

        if(!ValidationUtil.validateAnstiMutant(figure, gender)) {
            System.out.println("Someone tried to become mutant");
            return;
        }

        var details = player.getDetails();

        if(details == null) {
            throw new Exception("Details is null");
        }

        var wardrobe = WardrobeDao.getWardrobe(details.getId());

        if(wardrobe == null) {
            WardrobeDao.addWardrobe(details.getId(), slotId, figure, gender);
        } else {
            WardrobeDao.updateWardrobe(details.getId(), slotId, figure, gender);
        }
    }
}
