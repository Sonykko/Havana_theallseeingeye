package org.alexdev.http.game.housekeeping;

public class HousekeepingWordfilter {
    private final int id;
    private final String word;
    private final boolean isBannable;
    private final boolean isFilterable;

    public HousekeepingWordfilter(int id, String word, boolean isBannable, boolean isFilterable) {
        this.id = id;
        this.word = word;
        this.isBannable = isBannable;
        this.isFilterable = isFilterable;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public boolean isBannable() {
        return isBannable;
    }

    public boolean isFilterable() {
        return isFilterable;
    }
}
