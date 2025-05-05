package com.example.unimingle;

public class Event_Model {
    public int imageResId;
    public String title;
    public String location;
    public String date;
    public String description;
    public int currentPeople;
    public int totalPeople;

    public int userprofileResId;

    public Event_Model(int imageResId, String title, String location, String date, String description, int currentPeople, int totalPeople,int userprofileResId) {
        this.imageResId = imageResId;
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.currentPeople = currentPeople;
        this.totalPeople = totalPeople;
        this.userprofileResId = userprofileResId ;
    }
}
