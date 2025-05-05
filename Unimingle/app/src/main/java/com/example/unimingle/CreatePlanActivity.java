package com.example.unimingle;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreatePlanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText etEventName, etSlots, etDate, etDescription, etTime, etLocation;
    Button btnCreatePlan;
    DatabaseReference databasePlans;
    private Uri imageUri = null;
    ImageButton btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        // Firebase reference
        databasePlans = FirebaseDatabase.getInstance().getReference("plans");

        // Log current user UID
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d("CreatePlanActivity", "Current user UID: " + uid);
        } else {
            Log.d("CreatePlanActivity", "No user logged in!");
        }

        // Test write to plans node
        databasePlans.push().setValue("test_write")
            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Test write success", Toast.LENGTH_SHORT).show())
            .addOnFailureListener(e -> Toast.makeText(this, "Test write failed: " + e.getMessage(), Toast.LENGTH_LONG).show());

        // Link views
        etEventName = findViewById(R.id.etEventName);
        etSlots = findViewById(R.id.etSlots);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        etTime = findViewById(R.id.etTime);
        etLocation = findViewById(R.id.etLocation);
        btnCreatePlan = findViewById(R.id.btnCreatePlan);
        btnUpload = findViewById(R.id.btnUpload);

        // Show pickers
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit
        btnCreatePlan.setOnClickListener(v -> createPlan());

        btnUpload.setOnClickListener(v -> openGallery());
        btnUpload.setOnLongClickListener(v -> {
            imageUri = null;
            btnUpload.setImageResource(R.drawable.ic_upload);
            Toast.makeText(this, "Image removed!", Toast.LENGTH_SHORT).show();
            return true;
        });
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
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || slots.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databasePlans.push().getKey();
        String imageUrl = "";  // Placeholder until image upload is implemented
        Map<String, Boolean> participants = new HashMap<>(); // For future tracking

        Plan plan = new Plan(id, name, slots, date, time, location, description, imageUrl);
        plan.participants = participants;

        databasePlans.child(id).setValue(plan)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Plan Created", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally go back or clear form
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            btnUpload.setImageURI(imageUri);
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show();
        }
    }
}
