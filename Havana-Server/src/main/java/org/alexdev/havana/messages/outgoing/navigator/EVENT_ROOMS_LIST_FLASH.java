/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.game.events.EventsManager;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class EVENT_ROOMS_LIST_FLASH extends MessageComposer {

    private int categoryId;

    public EVENT_ROOMS_LIST_FLASH(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void compose(NettyResponse response) {

        if (categoryId < 1 || categoryId > 11) {
            categoryId = 0;
        }

        response.writeInt(categoryId); //category
        response.writeInt(12); //dont know
        response.writeString(""); //dont know

        var events = EventsManager.getInstance().getEvents(categoryId);

        response.writeInt(events.size()); //room count
        for(int i = 0; i < events.size(); i++) {
            var event = events.get(i);
            response.writeInt(event.getRoomId());
            response.writeBool(true); //isEvent

            response.writeString(event.getName());
            response.writeString(event.getEventHoster().getName());
            response.writeInt(event.getRoomData().getAccessTypeId());
            response.writeInt(event.getPlayersInEvent());
            response.writeInt(event.getRoomData().getVisitorsMax());
            response.writeString(event.getDescription());
            response.writeBool(true);
            response.writeBool(true);
            response.writeInt(event.getRoomData().getRating());
            response.writeInt(categoryId);
            response.writeString(event.getStartedDate());

            response.writeInt(event.getTags().size());
            for(int j = 0; j < event.getTags().size(); j++) {
                response.writeString(event.getTags().get(j));
            }

            var room = event.getRoomData();

            //TODO: icon
            var iconData = room.getIconData();
            var iconDataSplit = iconData.split("\\|");

            response.writeInt(Integer.parseInt(iconDataSplit[0])); //TODO: background img
            response.writeInt(Integer.parseInt(iconDataSplit[1])); //TODO: foreground img

            if(iconDataSplit.length > 2) {
                var itemsSplit = iconDataSplit[2].split(",");

                response.writeInt(itemsSplit.length - 1); //TODO: items count
                for(int k = 1; k < itemsSplit.length - 1; k++) {
                    var pairs = itemsSplit[k].split(" ");
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
    }

    @Override
    public short getHeader() {
        return 451;
    }
}
