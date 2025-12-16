package org.alexdev.havana.game.moderation.cfh.topics;

public class CFHTopics {
    private final int id;
    private String sanctionReasonId;
    private String sanctionReasonValue;
    private String sanctionReasonDesc;

    public CFHTopics(int id, String sanctionReasonId, String sanctionReasonValue, String sanctionReasonDesc) {
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

    public void setSanctionReasonId(String sanctionReasonId) {
        this.sanctionReasonId = sanctionReasonId;
    }

    public void setSanctionReasonValue(String sanctionReasonValue) {
        this.sanctionReasonValue = sanctionReasonValue;
    }

    public void setSanctionReasonDesc(String sanctionReasonDesc) {
        this.sanctionReasonDesc = sanctionReasonDesc;
    }
}

