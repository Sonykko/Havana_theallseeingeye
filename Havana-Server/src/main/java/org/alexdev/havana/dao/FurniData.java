package org.alexdev.havana.dao;

public class FurniData {
    private String itemType;
    private String furniId;
    private String saleCode;
    private String classId;
    private boolean isGroupable;
    private boolean isRecycleable;
    private boolean isTradeable;
    private String stuffData;
    private String name;
    private String description;

    public FurniData(String itemType, String furniId, String saleCode, String classId, boolean isGroupable, boolean isRecycleable, boolean isTradeable, String stuffData, String name, String description) {
        this.itemType = itemType;
        this.furniId = furniId;
        this.saleCode = saleCode;
        this.classId = classId;
        this.isGroupable = isGroupable;
        this.isRecycleable = isRecycleable;
        this.isTradeable = isTradeable;
        this.stuffData = stuffData;
        this.name = name;
        this.description = description;
    }

    public String getItemType() {
        return itemType;
    }

    public String getFurniId() {
        return furniId;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public String getClassId() {
        return classId;
    }

    public boolean getIsGroupable() {
        return isGroupable;
    }

    public boolean getIsRecycleable() {
        return isRecycleable;
    }

    public boolean getIsTradeable() {
        return isTradeable;
    }

    public String getStuffData() {
        return stuffData;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

//["s","1","rare_parasol*0","15444","0","1","1","#FFFFFF,#FFFFFF,#FFFFFF,#9EFF1C","Green Parasol","Block those rays!"]