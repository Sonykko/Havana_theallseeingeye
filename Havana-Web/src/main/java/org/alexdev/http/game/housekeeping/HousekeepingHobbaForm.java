package org.alexdev.http.game.housekeeping;

public class HousekeepingHobbaForm {
    private int id;
    private String habboname;
    private String email;
    private String firstname;
    private String lastname;
    private String pickedUp;
    private String timestamp;

    public HousekeepingHobbaForm(int id, String habboname, String email, String firstname, String lastname, String pickedUp, String timestamp) {
        this.id = id;
        this.habboname = habboname;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pickedUp = pickedUp;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getHabboname() {
        return habboname;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPickedUp() {
        return pickedUp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setPickedUp(String pickedUp) {
        this.pickedUp = pickedUp;
    }
}