package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText nameEditText, emailEditText;
    private Button eventsButton, guestsButton; // Add nav buttons

    private Button saveProfileButton, changePasswordButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize navigation buttons
        eventsButton = findViewById(R.id.eventsButton);
        guestsButton = findViewById(R.id.guestsButton);

        // Set up navigation click listeners
        eventsButton.setOnClickListener(v -> navigateToEvents());
        guestsButton.setOnClickListener(v -> navigateToGuests());

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveProfileButton = findViewById(R.id.saveProfileButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        logoutButton = findViewById(R.id.logout);

        // Set up logout button click listener
        logoutButton.setOnClickListener(v -> performLogout());

        // Load user data
        loadUserData();

        // Set click listeners
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                Intent intent = new Intent(EditProfileActivity.this, EventsListActivity.class);
                startActivity(intent);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to change password screen
                Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void navigateToEvents() {
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToGuests() {
        Intent intent = new Intent(this, GuestsListActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToProfile() {
        // Already on profile screen
        return;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            nameEditText.setText(currentUser.getDisplayName());
            emailEditText.setText(currentUser.getEmail());
        }
    }

    private void updateProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String newName = nameEditText.getText().toString();
            String newEmail = emailEditText.getText().toString();

            // Update Firebase user profile
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Update Firestore user document
                                DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("name", newName);
                                updates.put("email", newEmail);

                                userRef.update(updates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(EditProfileActivity.this,
                                                            "Profile Updated Successfully",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(EditProfileActivity.this,
                                                            "Failed to update Firestore",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(EditProfileActivity.this,
                                        "Failed to update profile",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void performLogout() {
        mAuth.signOut();
        // Create intent for login activity
        Intent intent = new Intent(EditProfileActivity.this, loginActivity.class);
        // Clear activity stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}