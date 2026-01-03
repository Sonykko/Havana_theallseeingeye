package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomVisitsDao;
import org.alexdev.havana.game.moderation.RoomVisit;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.Calendar;
import java.util.List;

public class FLASH_MODTOOLS_ROOM_VISITS_RESPONSE extends MessageComposer {

    private final int playerId;

    public FLASH_MODTOOLS_ROOM_VISITS_RESPONSE(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void compose(NettyResponse response) {
        List<RoomVisit> roomVisits = RoomVisitsDao.getVisits(playerId);

        response.writeInt(playerId);
        response.writeString(PlayerDao.getName(playerId));
        response.writeInt(roomVisits.size());

        for (RoomVisit roomVisit : roomVisits) {
            Room room = RoomManager.getInstance().getRoomById(roomVisit.getRoomId());
            if (room != null) {
                response.writeBool(room.isPublicRoom());
                response.writeInt(room.getId());
                response.writeString(room.getData().getName());
            } else {
                response.writeBool(false);
                response.writeInt(0);
                response.writeString("unknown room");
            }
            response.writeInt(roomVisit.getCalendar().get(Calendar.HOUR_OF_DAY));
            response.writeInt(roomVisit.getCalendar().get(Calendar.MINUTE));
        }
    }

    @Override
    public short getHeader() {
        return 537;
    }
}
