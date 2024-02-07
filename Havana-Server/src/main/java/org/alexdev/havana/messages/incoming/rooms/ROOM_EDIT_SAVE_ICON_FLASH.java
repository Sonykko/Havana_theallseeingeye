/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_SAVE_ICON_454_FLASH;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_SAVE_ICON_456_FLASH;
import org.alexdev.havana.messages.outgoing.rooms.ROOM_EDIT_SAVE_ICON_457_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class ROOM_EDIT_SAVE_ICON_FLASH implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        var room = player.getRoomUser().getRoom();

        if(room == null) {
            throw new Exception("COULD NOT FIND ROOM");
        }

        var junk = reader.readInt(); //always 3

        //FORMAT:
        //     bg      top      items (pos,item)
        //      8   |   0    |  0,25 1,25 2,25 3,25 4,17 5,25 7,25

        var iconData = new StringBuilder();

        var background = reader.readInt();

        iconData.append(background);
        iconData.append("|");

        var toplayer = reader.readInt();

        iconData.append(toplayer);
        iconData.append("|");

        var amountOfItems = reader.readInt();

        int lastItem = -1;

        for(int i = 0; i < amountOfItems; i++) {
            if(amountOfItems == 1) {
                if(i == 0 && i == amountOfItems - 1) {
                    iconData.append(reader.readInt());
                    iconData.append(",");
                    iconData.append(reader.readInt());
                    continue;
                }
            }

            if(amountOfItems >= 2) {
                if(i == 0) {
                    iconData.append(reader.readInt());
                    lastItem = reader.readInt();
                    iconData.append(",");
                    continue;
                }
            }

            var pos = reader.readInt();
            var item = reader.readInt();

            if (pos < 0 || pos > 10)
            {
                return;
            }

            if (item < 1 || item > 27)
            {
                return;
            }

            iconData.append(item);
            iconData.append(" ");

            iconData.append(pos);
            iconData.append(",");
        }

        if(lastItem != -1) {
            iconData.append(lastItem);
        }

        var result = iconData.toString();

        RoomDao.saveIcon(room.getId(), result);

        room.getData().setIconData(result);

        room.send(new ROOM_EDIT_SAVE_ICON_457_FLASH(room.getId()));
        room.send(new ROOM_EDIT_SAVE_ICON_456_FLASH(room.getId()));
        room.send(new ROOM_EDIT_SAVE_ICON_454_FLASH(room, player));
    }
}
