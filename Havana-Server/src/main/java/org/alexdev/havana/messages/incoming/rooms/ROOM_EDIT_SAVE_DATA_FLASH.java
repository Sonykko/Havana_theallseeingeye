/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.dao.mysql.TagDao;
import org.alexdev.havana.game.navigator.NavigatorCategory;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_SAVE_DATA_454_FLASH;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_SAVE_DATA_456_FLASH;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_SAVE_DATA_467_FLASH;
import org.alexdev.havana.messages.outgoing.rooms.SET_ROOM_INFO_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.ArrayList;

public class ROOM_EDIT_SAVE_DATA_FLASH implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var currentRoom = player.getRoomUser().getRoom();

        var roomId = reader.readInt();
        var name = reader.readString();
        var description = reader.readString();
        var doorMode = reader.readInt();
        var password = reader.readString();
        var maxUsers = reader.readInt();
        var allowFurniMoving = reader.readInt();//TODO
        var allowTrading = reader.readInt();//TODO
        var showOwnername = reader.readInt(); //TODO
        var categoryId = reader.readInt();

        var tagCount = reader.readInt();
        var tags = new ArrayList<String>();
        for(var i = 0; i < tagCount; i++) {
            var tag = reader.readString();
            tags.add(tag);
        }

        //TODO: Check player rank
        /*var rank = currentRoom.getCategory().getMinimumRoleSetFlat();
        if(rank.getRankId() > player.getDetails().getRank().getRankId()) {
            System.out.println("TODO: RANK ERROR THING");
            //                Session.SendNotif(LanguageLocale.GetValue("user.roomdata.rightserror"));
            //                CategoryId = 0;
            categoryId = 0;
        }*/

        if(tags.size() > 2) {
            return;
        }

        currentRoom.getData().setName(name);
        currentRoom.getData().setAccessType(doorMode);
        currentRoom.getData().setDescription(description);
        currentRoom.getData().setCategoryId(categoryId);
        currentRoom.getData().setPassword(password);

        currentRoom.getData().getTags().clear();

        TagDao.removeTags(0, currentRoom.getId(), 0);

        for(int i = 0; i < tags.size(); i++){
            currentRoom.getData().addTag(player.getDetails().getId(), tags.get(i));
        }

        currentRoom.getData().setVisitorsMax(maxUsers);

        player.send(new ROOM_EDIT_SAVE_DATA_467_FLASH(roomId));
        player.send(new ROOM_EDIT_SAVE_DATA_456_FLASH(roomId));
        player.send(new SET_ROOM_INFO_FLASH(currentRoom, true, false));

        RoomDao.save(currentRoom);
    }
}
