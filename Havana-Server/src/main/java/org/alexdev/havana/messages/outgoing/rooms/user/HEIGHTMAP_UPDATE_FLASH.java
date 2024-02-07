/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.rooms.user;

import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.models.RoomModel;
import org.alexdev.havana.messages.outgoing.rooms.HEIGHTMAP_UPDATE;

public class HEIGHTMAP_UPDATE_FLASH extends HEIGHTMAP_UPDATE {
    public HEIGHTMAP_UPDATE_FLASH(Room room, RoomModel roomModel) {
        super(room, roomModel);
    }

    @Override
    public short getHeader() {
        return 31; // "@_"
    }
}
