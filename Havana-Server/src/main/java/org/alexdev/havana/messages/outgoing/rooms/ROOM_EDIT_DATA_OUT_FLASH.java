/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.dao.mysql.RoomRightsDao;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class ROOM_EDIT_DATA_OUT_FLASH extends MessageComposer {

    private Room room;

    public ROOM_EDIT_DATA_OUT_FLASH(Room room) {
        this.room = room;
    }

    //THIS RUNS WHEN WE CLICK THE EDIT BUTTON

    @Override
    public void compose(NettyResponse response) {

        /*
         var_106.tags = [];
         var _loc2_:int = param1.readInteger();
         var _loc3_:int = 0;
         while(_loc3_ < _loc2_)
         {
            var_106.tags.push(param1.readString());
            _loc3_++;
         }
         var_106.controllers = [];
         var _loc4_:int = param1.readInteger();
         var _loc5_:int = 0;
         while(_loc5_ < _loc4_)
         {
            var_106.controllers.push(new FlatControllerData(param1));
            _loc5_++;
         }
         var_106.controllerCount = param1.readInteger();
         Logger.log("BOOORROSH: undefined");
         return true;
         */

        response.writeInt(room.getId());
        response.writeString(room.getData().getName());
        response.writeString(room.getData().getDescription());
        response.writeInt(room.getData().getAccessTypeId());
        response.writeInt(room.getData().getCategoryId());
        response.writeInt(room.getData().getVisitorsMax());
        response.writeInt(25); //TODO: Visitors max limit
        response.writeInt(1); //TODO: Show owner name
        response.writeInt(1); //TODO: Allow furni moving
        response.writeInt(1); //TODO: Allow trading

        response.writeInt(room.getData().getTags().size());
        for(var tag : room.getData().getTags()) {
            response.writeString(tag);
        }

        var rights = RoomRightsDao.getRoomRights(room.getData());

        response.writeInt(rights.size());

        for(var right : rights) {
            var playerWithRights = PlayerManager.getInstance().getPlayerById(right);

            if(playerWithRights != null) {
                response.writeInt(right);
                response.writeString(playerWithRights.getDetails().getName());
            }
        }

        response.writeInt(rights.size());
    }

    @Override
    public short getHeader() {
        return 465;
    }
}
