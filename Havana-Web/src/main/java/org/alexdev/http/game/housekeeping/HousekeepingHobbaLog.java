package org.alexdev.http.game.housekeeping;

import org.alexdev.havana.util.DateUtil;

public class HousekeepingHobbaLog {
    private final String action;
    private int userId;
    private int targetId;
    private String message;
    private String extraNotes;
    private long createdAt;

    public HousekeepingHobbaLog(String action, int userId, int targetId, String message, String extraNotes, long createdAt) {
        this.action = action;
        this.userId = userId;
        this.targetId = targetId;
        this.message = message;
        this.extraNotes = extraNotes;
        this.createdAt = createdAt;
    }

    public String getAction() {
        return action;
    }

    public int getUserId() {
        return userId;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getMessage() {
        return message;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String formatCreatedAt(String format) {
        return DateUtil.getDate(this.createdAt, format);
    }
}
