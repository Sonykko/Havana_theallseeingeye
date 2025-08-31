package org.alexdev.http.game.housekeeping;

public class HousekeepingCFH {
    private final int cfhId;
    private final int userId;
    private final String username;
    private final String reason;
    private final String roomName;
    private final int roomId;
    private final String createdTime;
    private final boolean deleted;
    private final String cryId;
    private final String moderator;
    private final String pickedTime;
    private final String action;
    private final String messageToUser;

    public HousekeepingCFH(int cfhId, int userId, String username, String reason, String roomName, int roomId, String createdTime, boolean deleted, String cryId, String moderator,
                           String pickedTime, String action, String messageToUser) {
        this.cfhId = cfhId;
        this.userId = userId;
        this.username = username;
        this.reason = reason;
        this.roomName = roomName;
        this.roomId = roomId;
        this.createdTime = createdTime;
        this.deleted = deleted;
        this.cryId = cryId;
        this.moderator = moderator;
        this.pickedTime = pickedTime;
        this.action = action;
        this.messageToUser = messageToUser;
    }

    public int getCfhId() {
        return cfhId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getCryId() {
        return cryId;
    }

    public String getModerator() {
        return moderator;
    }

    public String getPickedTime() {
        return pickedTime;
    }

    public String getAction() {
        return action;
    }

    public String getMessageToUser() {
        return messageToUser;
    }
}