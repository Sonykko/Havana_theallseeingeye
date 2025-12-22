package org.alexdev.havana.server.rcon.messages;

public enum RconHeader {
    REFRESH_LOOKS("refresh_looks"),
    CFH_PICK("cfh_pick"),
    CFH_REPLY("cfh_reply"),
    CFH_BLOCK("cfh_block"),
    CFH_FOLLOW("cfh_follow"),
    COPY_PRIVATE_ROOM("copy_private_room"),
    MOD_ROOM_KICK("mod_room_kick"),
    MOD_GIVE_BADGE("mod_give_badge"),
    MOD_STICKIE_DELETE("mod_stickie_delete"),
    REFRESH_CATALOGUE_PAGES("refresh_catalogue_pages"),
    REFRESH_SETTINGS("refresh_settings"),
    REFRESH_NAVIGATOR("refresh_navigator"),
    REFRESH_PRIVATE_ROOM("refresh_private_room"),
    REFRESH_WORDFILTER("refresh_wordfilter"),
    REFRESH_GAMESRANKS("refresh_gamesranks"),
    REFRESH_BOTS("refresh_bots"),
    REFRESH_CFH_TOPICS("refresh_cfh_topics"),
    MOD_ALERT_USER("mod_alert_user"),
    MOD_KICK_USER("mod_kick_user"),
    HOTEL_ALERT("hotel_alert"),
    REFRESH_CLUB("refresh_club"),
    REFRESH_TAGS("refresh_tags"),
    REFRESH_HAND("refresh_hand"),
    REFRESH_CREDITS("refresh_credits"),
    FRIEND_REQUEST("friendrequest"),
    REFRESH_MESSENGER_CATEGORIES("refreshmessengercategories"),
    REFRESH_TRADE_SETTING("refreshtrade"),
    GROUP_DELETED("groupdeleted"),
    REFRESH_GROUP("refreshgroup"),
    REFRESH_GROUP_PERMS("refreshgroupperms"),
    REFRESH_ADS("refreshads"),
    INFOBUS_POLL("infobuspoll"),
    INFOBUS_DOOR_STATUS("infobusdoorstatus"),
    REFRESH_ROOM_BADGES("refreshroombadges"),
    INFOBUS_END_EVENT("infobusendevent"),
    REFRESH_CATALOGUE_FRONTPAGE("refreshcataloguefrontpage"),
    CLEAR_PHOTO("clearphoto"),
    DISCONNECT_USER("disconnect"),
    REFRESH_STATISTICS("refreshstats");

    private final String rawHeader;

    RconHeader(String rawHeader) {
        this.rawHeader = rawHeader;
    }

    public String getRawHeader() {
        return rawHeader;
    }

    public static RconHeader getByHeader(String header) {
        for (var rconHeader : values()) {
            if (rconHeader.getRawHeader().equalsIgnoreCase(header)) {
                return rconHeader;
            }
        }

        return null;
    }
}
