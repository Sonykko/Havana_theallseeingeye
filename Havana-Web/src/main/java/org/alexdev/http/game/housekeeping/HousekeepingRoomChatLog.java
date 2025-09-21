package org.alexdev.http.game.housekeeping;

public class HousekeepingRoomChatLog implements ChatLog {
    private int id;
    private int userId;
    private String username;
    private String message;
    private String roomName;
    private String roomId;
    private long timestamp;
    private String logType;

    public HousekeepingRoomChatLog(int id, int userId, String username, String message, String roomName, String roomId, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.roomName = roomName;
        this.roomId = roomId;
        this.timestamp = timestamp;
    }

    @Override
    public long getDate() {
        return timestamp;
    }

    @Override
    public String getLogType() {
        return logType;
    }

    @Override
    public void setLogType(String logType) {
        this.logType = logType;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public String getRoomName() { return roomName; }
    public String getRoomId() { return roomId; }
    public long getTimestamp() { return timestamp; }
}