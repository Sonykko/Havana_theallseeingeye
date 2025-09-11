package org.alexdev.http.game.promotion;

public class HotCampaign {
    private final int id;
    private final String title;
    private final String description;
    private final String image;
    private final String url;
    private final String url_text;

    public HotCampaign(int id, String title, String description, String image, String url, String url_text) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.url = url;
        this.url_text = url_text;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlText() {
        return url_text;
    }
}
