package org.alexdev.http.game.housekeeping;

public class HousekeepingBotGuide {
    private String speechKey;
    private String response;
    private String speechTrigger;

    public HousekeepingBotGuide(String speechKey, String response, String speechTrigger) {
        this.speechKey = speechKey;
        this.response = response;
        this.speechTrigger = speechTrigger;
    }

    public String getSpeechKey() {
        return speechKey;
    }

    public String getResponse() {
        return response;
    }

    public String getSpeechTrigger() {
        return speechTrigger;
    }

    public String setSpeechKey(String speechKey) {
        return this.speechKey = speechKey;
    }

    public String setResponse(String response) {
        return this.response = response;
    }

    public String setSpeechTrigger(String speechTrigger) {
        return this.speechTrigger = speechTrigger;
    }
}
