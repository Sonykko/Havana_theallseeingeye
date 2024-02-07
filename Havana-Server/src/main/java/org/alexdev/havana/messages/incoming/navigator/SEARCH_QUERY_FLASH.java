/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */


package org.alexdev.havana.messages.incoming.navigator;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.navigator.SEARCH_QUERY_SEND_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class SEARCH_QUERY_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        String searchQuery = reader.readString();

        var rooms = RoomDao.searchRooms(searchQuery, 100, player);

        if(searchQuery.startsWith("user:")) {
            var split = searchQuery.split(":");
            if(split.length > 1) {
                var username = split[1].replace(" ", "");
                var searchPlayer = PlayerDao.getDetails(username);//PlayerManager.getInstance().getPlayerByName(username);
                if(searchPlayer != null) {
                    var playerRooms = RoomDao.getRoomsByUserId(searchPlayer.getId());

                    rooms = playerRooms;
                }
            }
        }

        RoomManager.getInstance().sortRooms(rooms);
        RoomManager.getInstance().ratingSantiyCheck(rooms);

        player.send(new SEARCH_QUERY_SEND_FLASH(player, rooms, searchQuery));
    }
}
