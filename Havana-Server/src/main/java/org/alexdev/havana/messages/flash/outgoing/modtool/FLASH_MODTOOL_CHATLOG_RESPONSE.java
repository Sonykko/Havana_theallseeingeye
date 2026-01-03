package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.dao.mysql.NavigatorDao;
import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.game.moderation.ChatMessage;
import org.alexdev.havana.game.player.PlayerDetails;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.Calendar;
import java.util.List;

public class FLASH_MODTOOL_CHATLOG_RESPONSE extends MessageComposer {

    PlayerDetails playerDetails;

    public FLASH_MODTOOL_CHATLOG_RESPONSE(PlayerDetails playerDetails) {
        this.playerDetails = playerDetails;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(playerDetails.getId());
        response.writeString(playerDetails.getName());
        List<Room> recentVisited = NavigatorDao.getRecentlyVisited(10, playerDetails.getId());
        response.writeInt(recentVisited.size());

        for (Room recentlyVisitedRoom : recentVisited) {
            response.writeBool(recentlyVisitedRoom.isPublicRoom());
            response.writeInt(recentlyVisitedRoom.getId());
            response.writeString(recentlyVisitedRoom.getData().getName());

            List<ChatMessage> chats = RoomDao.getModChatlog(recentlyVisitedRoom.getId());

            response.writeInt(chats.size());
            for (ChatMessage chatMessage : chats) {
                response.writeInt(chatMessage.getCalendar().get(Calendar.HOUR_OF_DAY));
                response.writeInt(chatMessage.getCalendar().get(Calendar.MINUTE));
                response.writeInt(chatMessage.getPlayerId());
                response.writeString(chatMessage.getPlayerName());
                response.writeString(chatMessage.getMessage());
            }

        }

    }

    @Override
    public short getHeader() {
        return 536;
    }
}