package com.example.unimingle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EventDetailsActivity extends AppCompatActivity {

    ImageView eventImage;
    TextView eventDate, eventTime, eventLocation, eventDescription;
    Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventImage = findViewById(R.id.eventImage);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        eventLocation = findViewById(R.id.eventLocation);
        eventDescription = findViewById(R.id.eventDescription);
        joinBtn = findViewById(R.id.joinBtn);

        // Get intent data
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String location = intent.getStringExtra("location");
        String description = intent.getStringExtra("description");
        String hostUid = intent.getStringExtra("hostUid");

        // Set event data if not null
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(eventImage);
        }

        if (date != null) eventDate.setText(date);
        if (time != null) eventTime.setText(time);
        if (location != null) eventLocation.setText(location);
        if (description != null) eventDescription.setText(description);

        joinBtn.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hostUid == null || hostUid.isEmpty()) {
                Toast.makeText(this, "Host information missing", Toast.LENGTH_SHORT).show();
                return;
            }

            String currentUid = currentUser.getUid();
            String message = "Hey! I'm interested in joining your event.";

            DatabaseReference chatRef = FirebaseDatabase.getInstance()
                    .getReference("Chats")
                    .child(hostUid)
                    .child(currentUid)
                    .push();

            HashMap<String, Object> msgData = new HashMap<>();
            msgData.put("sender", currentUid);
            msgData.put("receiver", hostUid);
            msgData.put("message", message);
            msgData.put("timestamp", System.currentTimeMillis());

            joinBtn.setEnabled(false); // prevent double taps

            chatRef.setValue(msgData).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Message sent to host!", Toast.LENGTH_SHORT).show();
                joinBtn.setEnabled(true);
                // Optionally navigate to ChatActivity
                // startActivity(new Intent(this, ChatActivity.class));
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                joinBtn.setEnabled(true);
            });
        });
    }
}
