/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages;

import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.room.Room;
import org.alexdev.havana.messages.incoming.TRYFLAT_FLASH;
import org.alexdev.havana.messages.incoming.handshake.HOMEROOM_FLASH;
import org.alexdev.havana.messages.incoming.handshake.INIT_CRYPTO_FLASH;
import org.alexdev.havana.messages.incoming.handshake.SSO;
import org.alexdev.havana.messages.incoming.inventory.GETSTRIP;
import org.alexdev.havana.messages.incoming.navigator.*;
import org.alexdev.havana.messages.incoming.rooms.*;
import org.alexdev.havana.messages.incoming.user.AVATAR_SAVE_FLASH;
import org.alexdev.havana.messages.incoming.user.GET_WARDROBE_FLASH;
import org.alexdev.havana.messages.incoming.user.badges.GETSELECTEDBADGES;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_BADGES;
import org.alexdev.havana.messages.outgoing.rooms.groups.GROUP_INFO;
import org.alexdev.havana.messages.outgoing.rooms.user.SAVE_WARDROBE_OUTFIT_FLASH;
import org.alexdev.havana.messages.outgoing.user.badges.USERBADGE_FLASH;

import java.util.HashMap;

public class MessageHandlerFlash extends MessageHandler {

    public MessageHandlerFlash() {
        super(true);

        registerFlashPackets();
    }

    private void registerFlashPackets() {
        registerEvent(415, new SSO());
        registerEvent(206, new INIT_CRYPTO_FLASH());
        registerEvent(384, new HOMEROOM_FLASH());
        registerEvent(387, new CAN_CREATE_ROOM_FLASH());

        registerEvent(59, new GOTOFLAT_FLASH());
        registerEvent(391, new TRYFLAT_FLASH());
        registerEvent(390, new GET_FLOORMAP_FLASH());
        registerEvent(126, new ROOM_LOAD_FLASH());

        registerEvent(430, new RECOMMENDED_ROOMS_FLASH());
        registerEvent(434, new OWN_ROOMS_INCOMING_FLASH());

        registerEvent(431, new ROOMS_HIGHEST_SCORES());
        registerEvent(432, new ROOMS_OWNED_BY_MY_FRIENDS_FLASH());
        registerEvent(433, new ROOMS_WHERE_MY_FRIENDS_ARE_FLASH());
        registerEvent(435, new ROOMS_MY_FAVOURITE_FLASH());
        registerEvent(436, new ROOMS_IVE_RECENTLY_VISITED());

        registerEvent(382, new SEARCH_TAGS_FLASH());
        registerEvent(437, new SEARCH_QUERY_FLASH());
        registerEvent(380, new PUBLIC_ROOMS_FLASH());
        registerEvent(439, new EVENT_ROOMS_FLASH());
        registerEvent(44, new AVATAR_SAVE_FLASH());
        registerEvent(375, new GET_WARDROBE_FLASH());
        registerEvent(438, new PUBLIC_ROOMS_SEARCH_FLASH());
        registerEvent(385, new GET_ROOM_INFO_FLASH());
        registerEvent(404, new GETSTRIP());
        registerEvent(400, new ROOM_EDIT_DATA_FLASH());
        registerEvent(401, new ROOM_EDIT_SAVE_DATA_FLASH());
        registerEvent(159, new GETSELECTEDBADGES(true));
        registerEvent(388, new GETPUBLICSPACE_FLASH());
        registerEvent(386, new ROOM_EDIT_SAVE_ICON_FLASH());
        registerEvent(376, new SAVE_WARDROBE_OUTFIT_FLASH());
    }
}
