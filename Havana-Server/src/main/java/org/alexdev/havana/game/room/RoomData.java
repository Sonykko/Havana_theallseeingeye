package org.alexdev.havana.game.room;

import org.alexdev.havana.dao.mysql.RoomDao;
import org.alexdev.havana.dao.mysql.TagDao;
import org.alexdev.havana.game.games.Game;
import org.alexdev.havana.game.games.GameManager;
import org.alexdev.havana.game.games.enums.GameType;
import org.alexdev.havana.game.room.handlers.walkways.WalkwaysManager;
import org.alexdev.havana.game.room.models.RoomModelTriggerType;
import org.alexdev.havana.util.StringUtil;
import org.alexdev.havana.util.config.GameConfiguration;

import java.util.List;

public class RoomData {
    //private static final int SECONDS_UNTIL_UPDATE = 60;
    private Room room;
    private int id;
    private int ownerId;
    private String ownerName;
    private int categoryId;
    private String name;
    private String description;
    private String model;
    private String ccts;
    private int wallpaper;
    private int floor;
    private String landscape;
    private boolean showOwnerName;
    private boolean superUsers;
    private boolean isGameArena;
    private String gameLobby;
    private int accessType;
    private String password;
    private int visitorsNow;
    private int visitorsMax;
    private int rating;
    private Game game;
    private String iconData;
    private int groupId;
    private boolean isHidden;
    private boolean isRoomMuted;
    private boolean isCustomRoom;
    private boolean legacyRoom;
    private boolean legacyLock;
    private int costPixels;
    private int costCoins;

    public RoomData(Room room) {
        this.room = room;
    }

    public void fill(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = 0;
        this.ccts = "";
        this.model = "";
        this.ownerName = "";
    }

    public void fill(int id, int ownerId, String ownerName, int category, String name, String description, String model, String ccts, int wallpaper, int floor, String decoration, boolean showName, boolean superUsers, int accessType, String password, int visitorsNow, int visitorsMax, int rating,
                     String iconData, int groupId, boolean isHidden, boolean legacyRoom, boolean legacyLock, int coinCost, int pixelCost) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerName = StringUtil.filterInput(ownerName, true);;
        this.categoryId = category;
        this.name = StringUtil.filterInput(name, true);
        this.description = StringUtil.filterInput(description, true);
        this.model = model;
        this.ccts = ccts;
        this.wallpaper = wallpaper;
        this.floor = floor;
        this.landscape = decoration;
        this.showOwnerName = showName;
        this.superUsers = superUsers;
        this.accessType = accessType;
        this.password = password;
        this.visitorsNow = visitorsNow;
        this.visitorsMax = visitorsMax;
        this.rating = rating;
        this.iconData = iconData;
        this.groupId = groupId;
        this.isHidden = isHidden;
        this.legacyRoom = legacyRoom;
        this.legacyLock = legacyLock;
        this.costCoins = coinCost;
        this.costPixels = pixelCost;

        if (WalkwaysManager.getInstance().getWalkways().stream().anyMatch(walkway -> walkway.getRoomTargetId() == this.room.getId())) {
            WalkwaysManager.getInstance().getWalkways().stream().filter(walkway -> walkway.getRoomTargetId() == this.room.getId()).findFirst().ifPresent(roomData -> this.room.setFollowRedirect(roomData.getRoomId()));
        }
    }

    public boolean isNavigatorHide() {
        if (GameConfiguration.getInstance().getBoolean("navigator.show.hidden.rooms")) {
            return false;
        }

        return this.isHidden;
    }

    public int getTotalVisitorsNow() {
        var childRooms = RoomManager.getInstance().getChildRooms(this.room);
        int totalVisitors = this.getVisitorsNow();

        if (childRooms.size() > 0) {
            for (Room room : childRooms) {
                totalVisitors += room.getData().getVisitorsNow();
            }
        }

        return totalVisitors;
    }

    public int getTotalVisitorsMax() {
        var childRooms = RoomManager.getInstance().getChildRooms(this.room);
        int totalMaxVisitors = this.getVisitorsMax();

        if (childRooms.size() > 0) {
            for (Room room : childRooms) {
                totalMaxVisitors += room.getData().getVisitorsMax();
            }
        }

        return totalMaxVisitors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public String getPublicName() {
        if (this.room.isPublicRoom()) {
            if (this.name.startsWith("Upper Hallways")) {
                return "Upper Hallways";
            }

            if (this.name.startsWith("Lower Hallways")) {
                return "Lower Hallways";
            }

            if (this.name.startsWith("Club Massiva")) {
                return "Club Massiva";
            }

            if (this.name.startsWith("The Chromide Club")) {
                return "The Chromide Club";
            }

            if (this.ccts.equals("hh_room_gamehall,hh_games")) {
                return "Cunning Fox Gamehall";
            }
        }

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCcts() {
        return ccts;
    }

    public void setCcts(String ccts) {
        this.ccts = ccts;
    }

    public int getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(int wallpaper) {
        this.wallpaper = wallpaper;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean showOwnerName() {
        return showOwnerName && !this.legacyRoom;
    }

    public void setShowOwnerName(boolean showName) {
        this.showOwnerName = showName;
    }

    public boolean allowSuperUsers() {
        return superUsers;
    }

    public void setSuperUsers(boolean superUsers) {
        this.superUsers = superUsers;
    }

    public String getAccessType() {
        if (this.accessType == 2) {
            return "password";
        }

        if (this.accessType == 1) {
            return "closed";
        }

        return "open";
    }

    public int getAccessTypeId() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVisitorsNow() {
        //return this.visitorsNow;
        int visitors = this.visitorsNow;

        if (this.room.getModel().getModelTrigger() != null && this.room.getModel().getModelTrigger() == RoomModelTriggerType.BATTLEBALL_LOBBY_TRIGGER) {
            visitors += GameManager.getInstance().getStartedGamesByType(GameType.BATTLEBALL).stream().mapToInt(game -> game.getActivePlayers().size()).findFirst().orElse(0);
            visitors += GameManager.getInstance().getStartedGamesByType(GameType.BATTLEBALL).stream().mapToInt(game -> game.getSpectators().size()).findFirst().orElse(0);
        }

        if (this.room.getModel().getModelTrigger() != null && this.room.getModel().getModelTrigger() == RoomModelTriggerType.SNOWSTORM_LOBBY_TRIGGER) {
            visitors += GameManager.getInstance().getStartedGamesByType(GameType.SNOWSTORM).stream().mapToInt(game -> game.getActivePlayers().size()).findFirst().orElse(0);
            visitors += GameManager.getInstance().getStartedGamesByType(GameType.SNOWSTORM).stream().mapToInt(game -> game.getSpectators().size()).findFirst().orElse(0);
        }

        return visitors;
    }

    public void setVisitorsNow(int visitorsNow) {
        this.visitorsNow = visitorsNow;
    }

    public int getVisitorsMax() {
        //return this.visitorsMax;
        int visitors = this.visitorsMax;

        if (this.room.getModel().getModelTrigger() != null && this.room.getModel().getModelTrigger() == RoomModelTriggerType.BATTLEBALL_LOBBY_TRIGGER) {
            visitors += GameManager.getInstance().getStartedGamesByType(GameType.BATTLEBALL).stream().mapToInt(game -> game.getMaxPlayers()).sum();
        }

        if (this.room.getModel().getModelTrigger() != null && this.room.getModel().getModelTrigger() == RoomModelTriggerType.SNOWSTORM_LOBBY_TRIGGER) {
            visitors += GameManager.getInstance().getStartedGamesByType(GameType.SNOWSTORM).stream().mapToInt(game -> game.getMaxPlayers()).sum();
        }

        return visitors;
    }

    public void setVisitorsMax(int visitorsMax) {
        this.visitorsMax = visitorsMax;
    }

    public int getRating(){
        return this.rating;
    }

    public void setRating(int amount){
        this.rating = amount;
    }

    public boolean isGameArena() {
        return isGameArena;
    }

    public void setGameArena(boolean gameArena) {
        isGameArena = gameArena;
    }

    public String getGameLobby() {
        return gameLobby;
    }

    public void setGameLobby(String gameLobby) {
        this.gameLobby = gameLobby;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getIconData() {
        return iconData;
    }

    public void setIconData(String iconData) {
        this.iconData = iconData;
    }

    public void addTag(int userId, String tag) {
        TagDao.addTag(0, this.id, groupId, tag);
    }

    public List<String> getTags() {
        return TagDao.getRoomTags(this.id);
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isRoomMuted() {
        return isRoomMuted;
    }

    public void setRoomMuted(boolean roomMuted) {
        isRoomMuted = roomMuted;
    }

    public boolean isCustomRoom() {
        return isCustomRoom;
    }

    public void setCustomRoom(boolean customRoom) {
        isCustomRoom = customRoom;
    }

    public boolean isLegacyRoom() {
        return this.legacyRoom;
    }

    public boolean isLegacyLocked() {
        return this.legacyLock;
    }

    public int getCoinCost() {
        return this.costCoins;
    }

    public int getPixelCost() {
        return this.costPixels;
    }

    public void setLegacyRoom(boolean b) {
        this.legacyRoom = b;
    }

    public void setLegacyLock(boolean b) {
        this.legacyLock = b;
    }

    public String getDescriptionPublicRoom() {
        return RoomDao.getDescriptionPublicRoom(this.getId());
    }
}
