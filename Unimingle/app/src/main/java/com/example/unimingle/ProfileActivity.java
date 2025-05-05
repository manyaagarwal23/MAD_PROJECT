package com.example.unimingle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage, hostedEventImage, btnBack;
    TextView tvNameAge, tvPronouns, tvBio, tvInterests;
    Button btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        hostedEventImage = findViewById(R.id.hostedEventImage);
        tvNameAge = findViewById(R.id.tvNameAge);
        tvPronouns = findViewById(R.id.tvPronouns);
        tvBio = findViewById(R.id.tvBio);
        tvInterests = findViewById(R.id.tvInterests);
        btnMessage = findViewById(R.id.btnMessage);
        btnBack = findViewById(R.id.btnBack);

        // Sample data (replace with actual dynamic values later)
        tvNameAge.setText("Ketan, 20");
        tvPronouns.setText("(he/him), Straight");
        tvBio.setText("Ambivert who enjoys playing football and exploring new things. Looking for casual connections...");
        tvInterests.setText("City Tours, Attending Concerts, Dancing, Lawn Tennis, Cooking, Petting animals");

        Picasso.get().load("https://example.com/profile.jpg").placeholder(R.drawable.userprofile).into(profileImage);
        Picasso.get().load("https://example.com/bpl_event.jpg").placeholder(R.drawable.bpl).into(hostedEventImage);

        btnBack.setOnClickListener(v -> finish());
    }
}
