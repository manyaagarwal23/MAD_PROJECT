package com.example.unimingle;

public class Event_Model {
    public String id;
    public int imageResId;
    public String imageUrl;  // Added for Firebase image URLs
    public String title;
    public String location;
    public String date;
    public String description;
    public int currentPeople;
    public int totalPeople;

    public int userprofileResId;
    public String hostUid;
    public String hostName;

    // Constructor for hardcoded events with resource IDs
    public Event_Model(int imageResId, String title, String location, String date, String description, 
                      int currentPeople, int totalPeople, int userprofileResId, String hostUid, String hostName) {
        this.id = "";  // Empty ID for hardcoded events
        this.imageResId = imageResId;
        this.imageUrl = "";  // No URL for hardcoded events
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.currentPeople = currentPeople;
        this.totalPeople = totalPeople;
        this.userprofileResId = userprofileResId;
        this.hostUid = hostUid;
        this.hostName = hostName;
    }

    // Constructor with ID for Firebase events
    public Event_Model(String id, int imageResId, String title, String location, String date, String description, 
                      int currentPeople, int totalPeople, int userprofileResId, String hostUid, String hostName) {
        this.id = id;
        this.imageResId = imageResId;
        this.imageUrl = "";  // No URL for resource-based events
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.currentPeople = currentPeople;
        this.totalPeople = totalPeople;
        this.userprofileResId = userprofileResId;
        this.hostUid = hostUid;
        this.hostName = hostName;
    }
    
    // Constructor with image URL for Firebase events
    public Event_Model(String id, String imageUrl, String title, String location, String date, String description, 
                      int currentPeople, int totalPeople, int userprofileResId, String hostUid, String hostName) {
        this.id = id;
        this.imageResId = R.drawable.bpl;  // Default image if URL fails to load
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.currentPeople = currentPeople;
        this.totalPeople = totalPeople;
        this.userprofileResId = userprofileResId;
        this.hostUid = hostUid;
        this.hostName = hostName;
    }
}
