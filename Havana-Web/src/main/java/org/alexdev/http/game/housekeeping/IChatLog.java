package org.alexdev.http.game.housekeeping;

public interface IChatLog {
    long getDate();
    String getLogType();
    void setLogType(String type);
}