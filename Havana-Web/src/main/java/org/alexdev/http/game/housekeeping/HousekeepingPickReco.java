package org.alexdev.http.game.housekeeping;

public class HousekeepingPickReco {
    private final int recommended_id;
    private final String type;
    private final String is_staff_pick;

    public HousekeepingPickReco(int recommended_id, String type, String is_staff_pick) {
        this.recommended_id = recommended_id;
        this.type = type;
        this.is_staff_pick = is_staff_pick;
    }

    public int getRecommendedId() {
        return recommended_id;
    }

    public String getType() {
        return type;
    }

    public String getIsStaffPick() {
        return is_staff_pick;
    }
}
