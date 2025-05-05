package com.example.unimingle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginBtn;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailField = findViewById(R.id.loginUsername);  // Assuming fullName is the email input field
        passwordField = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);

        // Check if the user is already logged in when the activity starts
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, go directly to HomeActivity
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();  // Prevent back navigation to login page
        }

        // Handle the login button click
        loginBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String pass = passwordField.getText().toString();

            // Validate email and password
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform login attempt with FirebaseAuth
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Successful login
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                            finish();  // Close the login activity
                        } else {
                            // Handle login failure
                            Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
