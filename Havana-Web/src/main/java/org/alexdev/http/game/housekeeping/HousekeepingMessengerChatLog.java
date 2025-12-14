package org.alexdev.http.game.housekeeping;

public class HousekeepingMessengerChatLog implements IChatLog {
    private String logType;
    private int id;
    private int userId;
    private String username;
    private String friendId;
    private String friendName;
    private String body;
    private long date;

    public HousekeepingMessengerChatLog(String logType, int id, int userId, String username, String friendId, String friendName, String body, long date) {
        this.logType = logType;
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.friendId = friendId;
        this.friendName = friendName;
        this.body = body;
        this.date = date;
    }

    @Override
    public long getDate() {
        return date;
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
    public String getFriendId() { return friendId; }
    public String getFriendName() { return friendName; }
    public String getBody() { return body; }
}