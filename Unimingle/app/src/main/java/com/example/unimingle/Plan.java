package com.example.unimingle;
import java.util.HashMap;
import java.util.Map;

public class Plan {
    public String id, name, slots, date, time, location, details;
    public Map<String, Boolean> participants = new HashMap<>();
    public String imageUrl;  // Assuming image URL is uploaded

    public Plan() {
        // Default constructor required for Firebase
    }

    public Plan(String id, String name, String slots, String date, String time, String location, String details, String imageUrl) {
        this.id = id;
        this.name = name;
        this.slots = slots;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        this.imageUrl = imageUrl;
    }
}
