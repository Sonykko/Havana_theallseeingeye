/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.incoming.rooms;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.game.room.RoomManager;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_BADGES;
import org.alexdev.havana.server.netty.streams.NettyRequest;

import java.util.HashMap;

public class GOTOFLAT_FLASH extends GOTOFLAT {

    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        int roomId = reader.readInt();

        if (player.getRoomUser().getAuthenticateId() != roomId) {
            return;
        }

        Room room = RoomManager.getInstance().getRoomById(roomId);

        if (room == null) {
            return;
        }

        var groupBadges = getRoomBadges(player);
        //var group = player.getJoinedGroup(player.getDetails().getFavouriteGroupId());

        //room.send(new GROUP_BADGES(groupBadges));

        room.getEntityManager().enterRoom(player, null);
    }

    private HashMap<Integer, String> getRoomBadges(Player player) {
        if (player.getRoomUser().getRoom() == null) {
            return null;
        }

        Room room = player.getRoomUser().getRoom();
        HashMap<Integer, String> groupBadges = new HashMap<>();

        for (Player p : room.getEntityManager().getPlayers()) {
            if (p.getDetails().getFavouriteGroupId() > 0) {
                if (groupBadges.containsKey(p.getDetails().getFavouriteGroupId())) {
                    continue;
                }

                var group = player.getJoinedGroup(p.getDetails().getFavouriteGroupId());

                if (group == null) {
                    continue;
                }

                groupBadges.put(group.getId(), group.getBadge());
            }
        }

        return groupBadges;
    }
}
