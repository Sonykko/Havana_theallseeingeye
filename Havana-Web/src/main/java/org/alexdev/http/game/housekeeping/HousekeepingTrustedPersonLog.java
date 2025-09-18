package org.alexdev.http.game.housekeeping;

public class HousekeepingTrustedPersonLog {
    private int id;
    private String username;
    private int userId;
    private String staff;
    private int type;
    private String timestamp;

    public HousekeepingTrustedPersonLog(int id, String username, int userId, String staff, int type, String timestamp) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.staff = staff;
        this.type = type;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public String getStaff() {
        return staff;
    }

    public int getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
