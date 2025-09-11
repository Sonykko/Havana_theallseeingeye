package org.alexdev.http.game.housekeeping;

public class HousekeepingBanner {
    private int id;
    private String text;
    private String banner;
    private String url;
    private int status;
    private int advanced;
    private int orderId;

    public HousekeepingBanner(int id, String text, String banner, String url, int status, int advanced, int orderId) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAdvanced(int advanced) {
        this.advanced = advanced;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}