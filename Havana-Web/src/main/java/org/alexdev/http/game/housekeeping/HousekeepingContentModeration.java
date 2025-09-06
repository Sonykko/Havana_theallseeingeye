package org.alexdev.http.game.housekeeping;

public class HousekeepingContentModeration {
    private final int id;
    private final String type;
    private final String object_id;
    private final String message;
    private final int is_moderated;
    private final String timestamp;
    private String value;

    public HousekeepingContentModeration(int id, String type, String object_id, String message, int is_moderated, String timestamp) {
        this.id = id;
        this.type = type;
        this.object_id = object_id;
        this.message = message;
        this.is_moderated = is_moderated;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getObjectId() {
        return object_id;
    }

    public String getMessage() {
        return message;
    }
    public int getIsModerated() {
        return is_moderated;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
