/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.managers.VoteData;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class SET_ROOM_INFO_FLASH extends MessageComposer {

    private Room room;
    private boolean unk;
    private boolean unk2;

    public SET_ROOM_INFO_FLASH(Room room, boolean unk, boolean unk2) {
        this.room = room;
        this.unk = unk;
        this.unk2 = unk2;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(unk); //response.writeInt(0);

        //TODO: Move to room serialize method
        //Room serialize
        response.writeInt(room.getData().getId());
        response.writeBool(false);
        response.writeString(room.getData().getName());

        if (this.room.getData().showOwnerName()) {
            response.writeString(this.room.getData().getOwnerName());
        } else {
            response.writeString("-");
        }

        //response.writeString(room.getData().getOwnerName());
        if(unk2 == false) {
            response.writeInt(room.getData().getAccessTypeId());
        } else {
            response.writeInt(0); //always leave open if we're being forwarded
        }
        response.writeInt(room.getData().getVisitorsNow());
        response.writeInt(room.getData().getVisitorsMax());
        response.writeString(room.getData().getDescription());
        response.writeInt(0); //TODO: srchSpecPrm [https://github.com/ntuative/RELEASE39-22643-22891-200911110035_07c3a2a30713fd5bea8a8caf07e33438/blob/cf5e02e174440a171426f9a73aa5a1fb02c2ee09/src/com/sulake/habbo/communication/messages/incoming/navigator/GuestRoomData.as
        response.writeBool(room.getCategory().hasAllowTrading()); //allow trading
        response.writeInt(room.getData().getRating());
        response.writeInt(room.getData().getCategoryId());
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


        //Room serialize finish
        //response.writeBool(true); //TODO: Room forward
        //response.writeBool(true); //TODO: Room forward
        response.writeBool(unk2); //TODO: Room forward
        //response.writeBool(false); //TODO: Room forward

    }

    @Override
    public short getHeader() {
        return 454;
    }
}
