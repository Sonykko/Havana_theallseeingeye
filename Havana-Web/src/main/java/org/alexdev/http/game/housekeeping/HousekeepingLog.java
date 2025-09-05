package org.alexdev.http.game.housekeeping;

public class HousekeepingLog {
    private final int id;
    private final int userId;
    private final String username;
    private final String description;
    private final String userIp;
    private final String date;
    private final String type;

    public HousekeepingLog(int id, int userId, String username, String description, String userIp, String date, String type) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.description = description;
        this.userIp = userIp;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getUserIp() {
        return userIp;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}