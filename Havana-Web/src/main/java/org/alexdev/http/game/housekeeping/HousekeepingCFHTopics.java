package org.alexdev.http.game.housekeeping;

public class HousekeepingCFHTopics {
    private final int id;
    private final String sanctionReasonId;
    private final String sanctionReasonValue;
    private final String sanctionReasonDesc;

    public HousekeepingCFHTopics(int id, String sanctionReasonId, String sanctionReasonValue, String sanctionReasonDesc) {
        this.id = id;
        this.sanctionReasonId = sanctionReasonId;
        this.sanctionReasonValue = sanctionReasonValue;
        this.sanctionReasonDesc = sanctionReasonDesc;
    }

    public int getId() {
        return id;
    }

    public String getSanctionReasonId() {
        return sanctionReasonId;
    }

    public String getSanctionReasonValue() {
        return sanctionReasonValue;
    }

    public String getSanctionReasonDesc() {
        return sanctionReasonDesc;
    }
}
