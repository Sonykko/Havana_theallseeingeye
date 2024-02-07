/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms;

import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class ROOM_EDIT_SAVE_ICON_454_FLASH extends MessageComposer {

    private Room room;
    private Player player;

    public ROOM_EDIT_SAVE_ICON_454_FLASH(Room room, Player player){
        this.room = room;
        this.player = player;
    }

    @Override
    public void compose(NettyResponse response) {
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

        response.writeBool(true);
    }

    @Override
    public short getHeader() {
        return 454;
    }
}
