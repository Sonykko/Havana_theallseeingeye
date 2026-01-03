package org.alexdev.havana.messages.flash.incoming.modtool;

import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.events.Event;
import org.alexdev.havana.game.events.EventsManager;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.events.ROOMEEVENT_INFO;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;
import org.alexdev.havana.util.config.GameConfiguration;

public class FLASH_MODTOOL_ROOM_ACTION implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.hasFuse(Fuseright.ROOM_ALERT)) {
            return;
        }

        int roomId = reader.readInt();
        boolean hasEvent = reader.readBoolean(); // has_event_txt
        boolean roomLock = reader.readBoolean(); // lock_check
        boolean roomChangename = reader.readBoolean(); // changename_check

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null || room.isPublicRoom()) {
            return;
        }

        if (roomLock) {
            room.getData().setAccessType(1);
        }

        if (roomChangename) {
            room.getData().setName(GameConfiguration.getInstance().getString("rcon.room.unacceptable.name"));
            room.getData().setDescription(GameConfiguration.getInstance().getString("rcon.room.unacceptable.desc"));
        }

        if (roomLock || roomChangename) {
            RoomDao.save(room);
        }

        if (hasEvent) {
            handleEvent(room);
        }
    }

    private void handleEvent(Room room) {
        if (!EventsManager.getInstance().hasEvent(room.getId())) {
            return;
        }

        Event event = EventsManager.getInstance().getEventByRoomId(room.getId());

        if (event == null) {
            return;
        }

        EventsManager.getInstance().removeEvent(event);
        room.send(new ROOMEEVENT_INFO(null));
    }
}