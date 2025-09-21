package org.alexdev.http.game.housekeeping;

import java.math.BigDecimal;

public class HousekeepingStickieNote {
    private String userName;
    private String text;
    private String createdAt;
    private String updatedAt;
    private BigDecimal id;
    private int roomId;

    public HousekeepingStickieNote(String userName, String text, String createdAt, String updatedAt, BigDecimal id, int roomId) {
        this.userName = userName;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = id;
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public BigDecimal getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }
}