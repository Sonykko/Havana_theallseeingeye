/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.List;

public class RECOMMENDED_ROOM_LIST_FLASH extends RECOMMENDED_ROOM_LIST{

    private final int mode;

    public RECOMMENDED_ROOM_LIST_FLASH(Player player, List<Room> roomList, int mode) {
        super(player, roomList);

        this.mode = mode;
    }

    @Override
    public void compose(NettyResponse response) {
        list(player, response, mode, roomList);
    }

    public static void list(Player player, NettyResponse response, int mode, List<Room> roomList) {
        if(mode >= -1) {
            response.writeInt(-1); //category
            response.writeInt(1); //some stuff
        } else if(mode == -2) {
            response.writeInt(0); //category
            response.writeInt(2); //some stuff
        }else if(mode == -3) {
            response.writeInt(0); //category
            response.writeInt(5); //some stuff
        }else if(mode == -4) {
            response.writeInt(0); //category
            response.writeInt(3); //some stuff
        }else if(mode == -5) {
            response.writeInt(0); //category
            response.writeInt(4); //some stuff
        }
        else if(mode == -6) {
            response.writeInt(0);
            response.writeInt(6); //favourites
        }

        response.writeString("");

        response.writeInt(roomList.size());

        for (Room room : roomList) {
            writeRoom(player, mode, response, room);
        }
    }

    public static void writeRoom(Player player, int mode, NettyResponse response, Room room) {
        //TODO: room serialize
        response.writeInt(room.getId());
        response.writeBool(false);
        response.writeString(room.getData().getName());

        if (room.isOwner(player.getDetails().getId()) || room.getData().showOwnerName() || player.hasFuse(Fuseright.SEE_ALL_ROOMOWNERS)) {
            response.writeString(room.getData().getOwnerName());
        } else {
            response.writeString("-");
        }

        response.writeInt(room.getData().getAccessTypeId());
        response.writeInt(room.getData().getVisitorsNow());
        response.writeInt(room.getData().getVisitorsMax());
        response.writeString(room.getData().getDescription());
        response.writeInt(1); //TODO: srchSpecPrm [https://github.com/ntuative/RELEASE39-22643-22891-200911110035_07c3a2a30713fd5bea8a8caf07e33438/blob/cf5e02e174440a171426f9a73aa5a1fb02c2ee09/src/com/sulake/habbo/communication/messages/incoming/navigator/GuestRoomData.as
        response.writeBool(true); //TODO: can trade
        response.writeInt(room.getData().getRating());
        response.writeInt(room.getData().getCategoryId()); //category
        response.writeString(""); //TODO: Event Creation Time

        response.writeInt(room.getData().getTags().size());
        for(var tag : room.getData().getTags()) {
            response.writeString(tag);
        }

        //TODO: icon
        var iconData = room.getData().getIconData();
        var iconDataSplit = iconData.split("\\|");

        response.writeInt(Integer.parseInt(iconDataSplit[0])); //TODO: background img
        response.writeInt(Integer.parseInt(iconDataSplit[1])); //TODO: foreground img

        if(iconDataSplit.length > 2) {
            var itemsSplit = iconDataSplit[2].split(",");

            response.writeInt(itemsSplit.length - 1); //TODO: items count
            for(int i = 1; i < itemsSplit.length - 1; i++) {
                var pairs = itemsSplit[i].split(" ");
                if(pairs.length > 1) {
                    var position = Integer.parseInt(pairs[1]);
                    var item = Integer.parseInt(pairs[0]);

                    response.writeInt(position);
                    response.writeInt(item);
                }
            }

            response.writeInt(Integer.parseInt(itemsSplit[0]));
            response.writeInt(Integer.parseInt(itemsSplit[itemsSplit.length - 1]));
        } else {
            response.writeInt(0); //TODO: items count
        }
    }

    @Override
    public short getHeader() {
        return 451; // "E_"
    }
}
