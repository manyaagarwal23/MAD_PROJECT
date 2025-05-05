package com.example.unimingle;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvUsername, tvName, tvBio, tvInterests;
    ImageView profileImage, hostedEventImage, editNameIcon, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvName = findViewById(R.id.tvName);
        tvBio = findViewById(R.id.tvBio);
        tvInterests = findViewById(R.id.tvInterests);
        profileImage = findViewById(R.id.profileImage);
        hostedEventImage = findViewById(R.id.hostedEventImage);
        editNameIcon = findViewById(R.id.editNameIcon);
        btnBack = findViewById(R.id.btnBack);

        // Set sample data
        tvUsername.setText("manya_23");
        tvName.setText("Manya Agarwal");
        tvBio.setText("Always ready for fun plans, meeting new people and creating fun memories.");
        tvInterests.setText("City Tours, Attending Concerts, Dancing, Lawn Tennis, Cooking, Petting animals");

        Picasso.get().load("https://example.com/manya_profile.jpg").placeholder(R.drawable.user_profile).into(profileImage);
        Picasso.get().load("https://example.com/event.jpg").placeholder(R.drawable.dance_event).into(hostedEventImage);

        btnBack.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
}
