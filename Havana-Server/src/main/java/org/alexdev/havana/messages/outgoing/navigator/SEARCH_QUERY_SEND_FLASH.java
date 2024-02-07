/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.incoming.navigator.SEARCH_QUERY_FLASH;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.List;

public class SEARCH_QUERY_SEND_FLASH extends MessageComposer {

    private Player player;
    private List<Room> roomList;
    private String query;

    public SEARCH_QUERY_SEND_FLASH(Player player, List<Room> roomList, String query) {
        this.player = player;
        this.roomList = roomList;
        this.query = query;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(1);
        response.writeInt(9);
        response.writeString(this.query);
        response.writeInt(this.roomList.size());

        for(int i = 0; i < this.roomList.size(); i++) {
            RECOMMENDED_ROOM_LIST_FLASH.writeRoom(this.player, 2, response, this.roomList.get(i));
        }
    }

    @Override
    public short getHeader() {
        return 451;
    }
}
