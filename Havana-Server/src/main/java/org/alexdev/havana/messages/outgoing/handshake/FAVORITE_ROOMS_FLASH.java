/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.handshake;

import org.alexdev.havana.dao.mysql.RoomFavouritesDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class FAVORITE_ROOMS_FLASH extends MessageComposer {

    private Player player;

    public FAVORITE_ROOMS_FLASH(Player player) {
        this.player = player;
    }

    @Override
    public void compose(NettyResponse response) {

        var favourites = RoomFavouritesDao.getFavouriteRooms(player.getDetails().getId(), false);

        response.writeInt(30); //limit

        response.writeInt(favourites.size());
        for(var id : favourites) {
            response.writeInt(id);
        }
    }

    @Override
    public short getHeader() {
        return 458;
    }
}
