package org.alexdev.http.game.housekeeping;

public class HousekeepingPickRecoDTO {
    private HousekeepingPickReco housekeepingPickReco;
    private String groupName;
    private String groupDescription;
    private String groupOwner;
    private Integer groupId;
    private String groupImage;
    private String isPicked;

    private String roomName;
    private String roomDescription;
    private String roomOwner;
    private Integer roomId;

    private Integer pickRecoId;
    private String type;

    public HousekeepingPickRecoDTO() {
    }

    public HousekeepingPickRecoDTO(HousekeepingPickReco housekeepingPickReco, String groupName, String groupDescription, String groupOwner, Integer groupId, String groupImage, String roomName, String roomDescription, String roomOwner, Integer roomId, String isPicked) {
        this.housekeepingPickReco = housekeepingPickReco;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupOwner = groupOwner;
        this.groupId = groupId;
        this.groupImage = groupImage;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomOwner = roomOwner;
        this.roomId = roomId;
        this.isPicked = isPicked;
    }

    public HousekeepingPickReco getHousekeepingPickReco() {
        return housekeepingPickReco;
    }

    public void setHousekeepingPickReco(HousekeepingPickReco housekeepingPickReco) {
        this.housekeepingPickReco = housekeepingPickReco;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getIsPicked() {
        return isPicked;
    }

    public void setIsPicked(String isPicked) {
        this.isPicked = isPicked;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getPickRecoId() {
        return pickRecoId;
    }

    public void setPickRecoId(Integer pickRecoId) {
        this.pickRecoId = pickRecoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}