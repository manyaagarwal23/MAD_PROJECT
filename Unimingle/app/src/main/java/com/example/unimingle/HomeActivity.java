package com.example.unimingle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Event_Adapter adapter;
    private List<Event_Model> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure this is the name of your XML file


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the event list
        eventList = new ArrayList<>();
        
        // Add a sample event (optional - you can remove this if you want to show only Firebase events)
        String currentUserUid = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        
        // Add a sample event (you can remove this if you want to show only Firebase events)
        eventList.add(new Event_Model(
                R.drawable.bpl,
                "BPL 2",
                "BML Munjal University",
                "22nd March 2025",
                "Attending this upcoming match. Would love to support the same team. Wanna Join?",
                3,
                4,
                R.drawable.userprofile,
                currentUserUid,
                "Ketan"
        ));

        // Set up the adapter
        adapter = new Event_Adapter(this, eventList);
        recyclerView.setAdapter(adapter);

        // Setup sidebar button clicks
        setupSidebar();

        // Store user email in Firebase Realtime Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserEmails");
            ref.child(user.getUid()).setValue(email);
            // Also store the reverse mapping:
            ref = FirebaseDatabase.getInstance().getReference("EmailToUid");
            ref.child(email.replace(".", ",")).setValue(user.getUid()); // Firebase keys can't have '.'
        }

        // Fetch plans from Firebase and add as event cards
        DatabaseReference plansRef = FirebaseDatabase.getInstance().getReference("plans");
        plansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing Firebase events but keep the sample event at index 0
                while (eventList.size() > 1) {
                    eventList.remove(1);
                }
                
                // Log how many plans were found
                Log.d("HomeActivity", "Found " + snapshot.getChildrenCount() + " plans in Firebase");
                
                // Add each plan from Firebase
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Plan plan = ds.getValue(Plan.class);
                    if (plan != null) {
                        Log.d("HomeActivity", "Processing plan: " + plan.name + " with ID: " + plan.id);
                        
                        // Parse slots (e.g., "3/4")
                        int currentPeople = 0, totalPeople = 0;
                        if (plan.slots != null && plan.slots.contains("/")) {
                            String[] parts = plan.slots.split("/");
                            try {
                                currentPeople = Integer.parseInt(parts[0].trim());
                                totalPeople = Integer.parseInt(parts[1].trim());
                            } catch (Exception e) {
                                Log.e("HomeActivity", "Error parsing slots: " + plan.slots, e);
                            }
                        }
                        
                        // Create event model from plan
                        Event_Model event;
                        if (plan.imageUrl != null && !plan.imageUrl.isEmpty()) {
                            // Use the constructor with image URL
                            event = new Event_Model(
                                    plan.id,
                                    plan.imageUrl,
                                    plan.name,
                                    plan.location,
                                    plan.date,
                                    plan.description,
                                    currentPeople,
                                    totalPeople,
                                    R.drawable.userprofile,
                                    plan.ownerUid != null ? plan.ownerUid : "",
                                    "Host"
                            );
                        } else {
                            // Use the constructor with default image resource
                            event = new Event_Model(
                                    plan.id,
                                    R.drawable.bpl,
                                    plan.name,
                                    plan.location,
                                    plan.date,
                                    plan.description,
                                    currentPeople,
                                    totalPeople,
                                    R.drawable.userprofile,
                                    plan.ownerUid != null ? plan.ownerUid : "",
                                    "Host"
                            );
                        }
                        
                        // Add the event to the list
                        eventList.add(event);
                        Log.d("HomeActivity", "Added event to list: " + event.title);
                    }
                }
                
                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
                Log.d("HomeActivity", "Adapter notified with " + eventList.size() + " events");
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeActivity", "Firebase error: " + error.getMessage());
                Toast.makeText(HomeActivity.this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSidebar() {
        ImageButton btnChat = findViewById(R.id.btn_chat);
        ImageButton btnEvents = findViewById(R.id.btn_events);
        ImageButton btn_user_profile = findViewById(R.id.btn_user_profile);
        ImageButton btnNotifications = findViewById(R.id.btn_notifications); // changed from btn_map to btn_notifications
        ImageButton btnPlanner = findViewById(R.id.btn_planner);


        btnChat.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, ChatListActivity.class)));


        btnEvents.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, CreatePlanActivity.class)));

        btn_user_profile.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class)));

        btnNotifications.setOnClickListener(view ->
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show());

        btnPlanner.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, PlansActivity.class)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ... rest of your code
    }
}
