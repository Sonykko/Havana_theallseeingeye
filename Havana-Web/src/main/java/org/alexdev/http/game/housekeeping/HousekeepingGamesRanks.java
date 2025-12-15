package org.alexdev.http.game.housekeeping;

import org.alexdev.http.game.housekeeping.enums.GameType;

public class HousekeepingGamesRanks {
    private int id;
    private GameType type;
    private String title;
    private int minPoints;
    private int maxPoints;

    public HousekeepingGamesRanks(int id, GameType type, String title, int minPoints, int maxPoints) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public int getId() {
        return id;
    }

    public GameType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
}
