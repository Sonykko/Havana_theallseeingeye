package org.alexdev.http.game.housekeeping;

public interface ChatLog {
    long getDate();
    String getLogType();
    void setLogType(String type);
}