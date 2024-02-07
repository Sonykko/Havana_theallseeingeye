/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.navigator.NavigatorManager;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.Collections;
import java.util.List;

public class PUBLIC_ROOMS_LIST_FLASH extends MessageComposer {

    private List<Room> roomList;

    public PUBLIC_ROOMS_LIST_FLASH(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(roomList.size());

        Collections.sort(roomList, (r1, r2) -> r2.getData().getVisitorsNow() - r1.getData().getVisitorsNow());

        for(int i = 0; i < roomList.size(); i++) {
            var room = roomList.get(i);
            response.writeInt(room.getId()); //nodeid
            response.writeString(room.getData().getName());
        }

        var navigatorStylesAmount = NavigatorManager.getInstance().getNavigatorStylesAmount();

        response.writeInt(navigatorStylesAmount); //some other length?

        for(int i = 0; i < roomList.size(); i++) {
            var room = roomList.get(i);

            var navigatorStyle = NavigatorManager.getInstance().getNavigatorStyle(room.getId());

            if(navigatorStyle == null) {
                continue;
            }

            response.writeString(room.getData().getPublicName());
            response.writeString(navigatorStyle.getDescription());
            response.writeInt(1); //show details
            response.writeString(navigatorStyle.getThumbnailUrl());
            response.writeString(navigatorStyle.getThumbnailUrl());
            response.writeInt(room.getData().getVisitorsNow()); //usercount
            response.writeInt(3); //type
            response.writeString(""); //unitpropertyset
            response.writeInt(0); //unitport
            response.writeInt(0); //worldid
            response.writeString(""); //castlibs
            response.writeInt(room.getData().getVisitorsMax());
            response.writeInt(room.getId()); //nodeId
        }
    }

    @Override
    public short getHeader() {
        return 450;
    }
}