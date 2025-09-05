package org.alexdev.http.game.housekeeping;

public class HousekeepingLoginLog {
    private final int id;
    private final int userId;
    private final String username;
    private final String userIp;
    private final String date;

    public HousekeepingLoginLog(int id, int userId, String username, String userIp, String date) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userIp = userIp;
        this.date = date;
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

    public String getUserIp() {
        return userIp;
    }

    public String getDate() {
        return date;
    }
}