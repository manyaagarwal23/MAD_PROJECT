package com.example.unimingle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlansActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PlanAdapter adapter;
    List<Plan> plans = new ArrayList<>();
    FirebaseDatabase db;
    DatabaseReference ref;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans_activity);

        recyclerView = findViewById(R.id.planRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlanAdapter(plans);
        recyclerView.setAdapter(adapter);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("plans");

        findViewById(R.id.createPlanBtn).setOnClickListener(v -> {
            startActivity(new Intent(this, CreatePlanActivity.class));
        });

        loadPlans();
    }

    private void loadPlans() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plans.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Plan plan = snap.getValue(Plan.class);
                    if (plan != null && plan.participants != null && plan.participants.containsKey(uid)) {
                        plans.add(plan);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlansActivity.this, "Failed to load plans", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
