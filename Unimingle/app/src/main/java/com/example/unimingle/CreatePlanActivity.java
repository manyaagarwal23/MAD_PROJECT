package com.example.unimingle;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreatePlanActivity extends AppCompatActivity {

    EditText etEventName, etSlots, etDate, etTime, etLocation, etDetails;
    Button btnCreate, btnUpload;
    DatabaseReference databasePlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        // Firebase reference
        databasePlans = FirebaseDatabase.getInstance().getReference("plans");

        // Link views
        etEventName = findViewById(R.id.etEventName);
        etSlots = findViewById(R.id.etSlots);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etLocation = findViewById(R.id.etLocation);
        etDetails = findViewById(R.id.etDetails);
        btnCreate = findViewById(R.id.btnCreate);
        btnUpload = findViewById(R.id.btnUpload); // Optional image handling later

        // Show pickers
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit
        btnCreate.setOnClickListener(v -> createPlan());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) ->
                        etDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePicker = new TimePickerDialog(this,
                (view, hourOfDay, minute) ->
                        etTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePicker.show();
    }

    private void createPlan() {
        String name = etEventName.getText().toString().trim();
        String slots = etSlots.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String details = etDetails.getText().toString().trim();

        if (name.isEmpty() || slots.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databasePlans.push().getKey();
        String imageUrl = "";  // Placeholder until image upload is implemented
        Map<String, Boolean> participants = new HashMap<>(); // For future tracking

        Plan plan = new Plan(id, name, slots, date, time, location, details, imageUrl);
        plan.participants = participants;

        databasePlans.child(id).setValue(plan)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Plan Created", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally go back or clear form
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
