package org.alexdev.havana.messages.incoming.user.badges;

import org.alexdev.havana.game.badges.Badge;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.user.badges.USERBADGE;
import org.alexdev.havana.messages.outgoing.user.badges.USERBADGE_FLASH;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class SETBADGE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        System.out.println("We're unequiping all badges");

        // Unequip all previous badges
        for (Badge badge : player.getBadgeManager().getBadges()) {
            player.getBadgeManager().changeBadge(badge.getBadgeCode(), false, 0);
        }

        // Equip new badges
        while (reader.contents().length() > 0) {
            int slotId = reader.readInt();
            String badgeCode = reader.readString();
            System.out.println("We're adding new badges, slotId: " + slotId + " badgeCode: " + badgeCode);

            if (slotId > 0 && slotId < 6 && badgeCode.length() > 0) {
                player.getBadgeManager().changeBadge(badgeCode, true, slotId);
                System.out.println("slotid > 0 && slotid < 6 && badgeCode.length() > 0");
            }
        }

        System.out.println("We're notifying users of badge updates " + player.getBadgeManager().getEquippedBadges().size());
        // Notify users of badge updates
        if (player.getRoomUser().getRoom() != null) {
            if(player.flash) {
                player.getRoomUser().getRoom().send(new USERBADGE_FLASH(player.getDetails().getId(), player.getBadgeManager().getEquippedBadges()));
            }
            else {
                player.getRoomUser().getRoom().send(new USERBADGE(player.getDetails().getId(), player.getBadgeManager().getEquippedBadges()));
            }
        }

        System.out.println("We're refreshing badges");
        player.getBadgeManager().refreshBadges();
        player.getBadgeManager().saveQueuedBadges();
    }
}
