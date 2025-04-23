package com.example.unimingle;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Event_Adapter adapter;
    private List<Event_Model> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Correct DrawerLayout reference
        drawerLayout = findViewById(R.id.drawerLayout);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        eventList.add(new Event_Model(R.drawable.bg_start, "BPL 2", "BML Munjal University", "22nd March 2025", "Attending this upcoming match... Wanna Join?", 3, 4));

        adapter = new Event_Adapter(this, eventList);
        recyclerView.setAdapter(adapter);

        // Sidebar button setup
        setupSidebar();
    }

    private void setupSidebar() {
        ImageButton btnChat = findViewById(R.id.btn_chat);
        ImageButton btnEvents = findViewById(R.id.btn_events);
        ImageButton btnGroups = findViewById(R.id.btn_groups);
        ImageButton btnMap = findViewById(R.id.btn_map);
        ImageButton btnPlanner = findViewById(R.id.btn_planner);

        btnChat.setOnClickListener(view -> Toast.makeText(this, "Chat clicked", Toast.LENGTH_SHORT).show());
        btnEvents.setOnClickListener(view -> Toast.makeText(this, "Events clicked", Toast.LENGTH_SHORT).show());
        btnGroups.setOnClickListener(view -> Toast.makeText(this, "Groups clicked", Toast.LENGTH_SHORT).show());
        btnMap.setOnClickListener(view -> Toast.makeText(this, "Map clicked", Toast.LENGTH_SHORT).show());
        btnPlanner.setOnClickListener(view -> Toast.makeText(this, "Planner clicked", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
