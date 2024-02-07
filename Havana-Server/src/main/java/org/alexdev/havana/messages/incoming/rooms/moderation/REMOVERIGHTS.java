package org.alexdev.havana.messages.incoming.rooms.moderation;

import org.alexdev.havana.dao.mysql.PlayerDao;
import org.alexdev.havana.dao.mysql.RoomRightsDao;
import org.alexdev.havana.game.fuserights.Fuseright;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.outgoing.rooms.RIGHTS_REMOVED_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.ArrayList;
import java.util.List;

public class REMOVERIGHTS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        if (!room.isOwner(player.getDetails().getId()) && !player.hasFuse(Fuseright.ANY_ROOM_CONTROLLER)) {
            return;
        }

        List<Integer> targets;

        if(player.flash) {
            targets = new ArrayList<Integer>();
            var length = reader.readInt();
            for(int i = 0; i < length; i++) {
                var targetId = reader.readInt();
                targets.add(targetId);
            }
        } else {
            targets = List.of(PlayerDao.getId(reader.contents()));
        }

        for (int targetId : targets) {
            if (!room.getRights().contains(targetId)) {
                continue;
            }

            var target = PlayerManager.getInstance().getPlayerById(targetId);

            if (target != null) {
                if (target.getRoomUser().getRoom() == null || target.getRoomUser().getRoom().getId() != room.getId()) {
                    continue;
                }

                room.getRights().remove(Integer.valueOf(targetId));

                if(player.flash) {
                    room.send(new RIGHTS_REMOVED_FLASH(room.getId(), targetId));
                }

                room.refreshRights(target, true);
            }

            RoomRightsDao.removeRights(targetId, room.getData());
        }
    }
}