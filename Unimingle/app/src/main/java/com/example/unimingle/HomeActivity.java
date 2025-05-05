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

        // Sample event data
        String hostUid = "";
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
            hostUid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        eventList = new ArrayList<>();
        eventList.add(new Event_Model(
                R.drawable.bpl,
                "BPL 2",
                "BML Munjal University",
                "22nd March 2025",
                "Attending this upcoming match. Would love to support the same team. Wanna Join?",
                3,
                4,
                R.drawable.userprofile,
                hostUid, // hostUid is now the real UID
                "Ketan" // hostName
        ));

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
            // Optionally, also store the reverse mapping:
            ref = FirebaseDatabase.getInstance().getReference("EmailToUid");
            ref.child(email.replace(".", ",")).setValue(user.getUid()); // Firebase keys can't have '.'
        }

        String emailToFind = "user@example.com";
        DatabaseReference refToFind = FirebaseDatabase.getInstance().getReference("EmailToUid");
        refToFind.child(emailToFind.replace(".", ",")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uid = snapshot.getValue(String.class);
                // Now you have the UID for this email
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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
