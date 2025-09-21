package org.alexdev.http.game.housekeeping;

public class HousekeepingCataloguePage {
    private int id;
    private int parentId;
    private int orderId;
    private String minRole;
    private int minRoleId;
    private int isNavigatable;
    private int isClubOnly;
    private String name;
    private int icon;
    private int colour;
    private String layout;
    private String images;
    private String texts;
    private String seasonalStart;
    private int seasonalLength;

    public HousekeepingCataloguePage() {
    }

    public HousekeepingCataloguePage(int id, int parentId, int orderId, String minRole, int minRoleId,
                                     int isNavigatable, int isClubOnly, String name, int icon, int colour,
                                     String layout, String images, String texts, String seasonalStart,
                                     int seasonalLength) {
        this.id = id;
        this.parentId = parentId;
        this.orderId = orderId;
        this.minRole = minRole;
        this.minRoleId = minRoleId;
        this.isNavigatable = isNavigatable;
        this.isClubOnly = isClubOnly;
        this.name = name;
        this.icon = icon;
        this.colour = colour;
        this.layout = layout;
        this.images = images;
        this.texts = texts;
        this.seasonalStart = seasonalStart;
        this.seasonalLength = seasonalLength;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getMinRole() {
        return minRole;
    }

    public int getMinRoleId() {
        return minRoleId;
    }

    public int getIsNavigatable() {
        return isNavigatable;
    }

    public int getIsClubOnly() {
        return isClubOnly;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColour() {
        return colour;
    }

    public String getLayout() {
        return layout;
    }

    public String getImages() {
        return images;
    }

    public String getTexts() {
        return texts;
    }

    public String getSeasonalStart() {
        return seasonalStart;
    }

    public int getSeasonalLength() {
        return seasonalLength;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setMinRole(String minRole) {
        this.minRole = minRole;
    }

    public void setMinRoleId(int minRoleId) {
        this.minRoleId = minRoleId;
    }

    public void setIsNavigatable(int isNavigatable) {
        this.isNavigatable = isNavigatable;
    }

    public void setIsClubOnly(int isClubOnly) {
        this.isClubOnly = isClubOnly;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public void setSeasonalStart(String seasonalStart) {
        this.seasonalStart = seasonalStart;
    }

    public void setSeasonalLength(int seasonalLength) {
        this.seasonalLength = seasonalLength;
    }
}