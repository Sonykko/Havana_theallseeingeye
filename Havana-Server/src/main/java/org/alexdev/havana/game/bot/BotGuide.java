package org.alexdev.havana.game.bot;

public class BotGuide {
    private String speechKey;
    private String response;
    private String speechTrigger;

    public BotGuide(String speechKey, String response, String speechTrigger) {
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
}
