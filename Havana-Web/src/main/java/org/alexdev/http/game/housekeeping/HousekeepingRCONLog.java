package org.alexdev.http.game.housekeeping;

public class HousekeepingRCONLog {
    private final int id;
    private final String type;
    private final String user;
    private final String moderator;
    private final String message;
    private final String timestamp;

    public HousekeepingRCONLog(int id, String type, String user, String moderator, String message, String timestamp) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.moderator = moderator;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getModerator() {
        return moderator;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
