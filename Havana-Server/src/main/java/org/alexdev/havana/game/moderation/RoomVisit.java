package org.alexdev.havana.game.moderation;

import java.util.Calendar;

public class RoomVisit {

    private final int playerId;
    private final int roomId;
    private final long visitedAt;

    public RoomVisit(int playerId, int roomId, long visitedAt) {
        this.playerId = playerId;
        this.roomId = roomId;
        this.visitedAt = visitedAt;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getRoomId() {
        return roomId;
    }

    public long getVisitedAt() {
        return visitedAt;
    }

    public Calendar getCalendar() {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.visitedAt * 1000L);
        return calendar;
    }
}