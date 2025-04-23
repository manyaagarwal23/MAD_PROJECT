package com.example.unimingle;

import java.util.Map;

public class Plan {
    public String id, name, slots, date, time, location, details;


    public Map<String, Boolean> participants;
    public int imageUrl ;

    // Required empty constructor for Firebase
    public Plan() {}

    public Plan(String id, String name, String slots, String date, String time, String location, String details, Map<String, Boolean> participants) {
        this.id = id;
        this.name = name;
        this.slots = slots;
        this.date = date;
        this.time = time;
        this.location = location;
        this.details = details;
        this.participants = null;
    }

    public Plan(String id, String name, String slots, String date, String time, String location, String details) {
    }
}

