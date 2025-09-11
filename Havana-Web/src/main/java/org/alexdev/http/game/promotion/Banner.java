package org.alexdev.http.game.promotion;

public class Banner {
    private final int id;
    private final String text;
    private final String banner;
    private final String url;
    private final int status;
    private final int advanced;
    private final int orderId;

    public Banner(int id, String text, String banner, String url, int status, int advanced, int orderId) {
        this.id = id;
        this.text = text;
        this.banner = banner;
        this.url = url;
        this.status = status;
        this.advanced = advanced;
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getBanner() {
        return banner;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public int getAdvanced() {
        return advanced;
    }

    public int getOrderId() {
        return orderId;
    }
}
