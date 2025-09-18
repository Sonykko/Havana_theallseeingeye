package org.alexdev.http.game.housekeeping;

public class HousekeepingRankVar {
    private int id;
    private String name;
    private String badge;
    private String description;

    public HousekeepingRankVar() {
    }

    public HousekeepingRankVar(int id, String name, String badge, String description) {
        this.id = id;
        this.name = name;
        this.badge = badge;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBadge() {
        return badge;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
