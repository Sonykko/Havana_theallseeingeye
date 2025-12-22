package org.alexdev.http.game.housekeeping;

import org.alexdev.havana.game.pathfinder.Position;

import java.util.ArrayList;
import java.util.List;

public class HousekeepingBot {
    private int id;
    private String name;
    private String mission;
    private Position startPosition;
    private String figure;
    private String figureFlash;
    private int roomId;

    private List<Position> walkspace;
    private String speeches;
    private String responses;
    private String unrecognisedSpeech;
    private String drinks;

    public HousekeepingBot(int id, String name, String mission, String figure, String figureFlash, int roomId) {
        this.id = id;
        this.name = name;
        this.mission = mission;
        this.figure = figure;
        this.figureFlash = figureFlash;
        this.roomId = roomId;
        this.startPosition = new Position();
        this.walkspace = new ArrayList<>();
        this.speeches = "";
        this.responses = "";
        this.unrecognisedSpeech = "";
        this.drinks = "";
    }

    public HousekeepingBot(int id, String name, String mission, int x, int y, int headRotation, int bodyRotation, String figure, String figureFlash, int roomId, String walkspaceData,
                           String speeches, String responses, String unrecognisedSpeech, String drinks) {
        this.id = id;
        this.name = name;
        this.mission = mission;
        this.startPosition = new Position(x, y, 0, headRotation, bodyRotation);
        this.figure = figure;
        this.figureFlash = figureFlash;
        this.roomId = roomId;
        this.walkspace = new ArrayList<>();

        for (String positionDatas : walkspaceData.split(" ")) {
            String[] positionData = positionDatas.split(",");
            this.walkspace.add(new Position(Integer.parseInt(positionData[0]), Integer.parseInt(positionData[1])));
        }

        if (this.walkspace.stream().noneMatch(position -> position.getX() == this.startPosition.getX() && position.getY() == this.startPosition.getY())) {
            this.walkspace.add(new Position(this.startPosition.getX(), this.startPosition.getY()));
        }

        this.speeches = speeches;
        this.responses = responses;
        this.unrecognisedSpeech = unrecognisedSpeech;
        this.drinks = drinks;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMission() {
        return mission;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public String getFigure() {
        return figure;
    }

    public String getFigureFlash() {
        return figureFlash;
    }

    public int getRoomId() {
        return roomId;
    }

    public List<Position> getWalkspace() {
        return walkspace;
    }

    public String getSpeeches() {
        return speeches;
    }

    public String getResponses() {
        return responses;
    }

    public String getUnrecognisedSpeech() {
        return unrecognisedSpeech;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public void setFigureFlash(String figureFlash) {
        this.figureFlash = figureFlash;
    }

    public void setWalkspace(List<Position> walkspace) {
        this.walkspace = walkspace;
    }

    public void setSpeeches(String speeches) {
        this.speeches = speeches;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    public void setUnrecognisedSpeech(String unrecognisedSpeech) {
        this.unrecognisedSpeech = unrecognisedSpeech;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }
}
