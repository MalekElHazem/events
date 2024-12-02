package com.example.android_project;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class AddGuestActivity extends AppCompatActivity {

    private EditText nameEditText, contactEditText;
    private Button addButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_guest);

        // Initialize views
        nameEditText = findViewById(R.id.titleEditText);
        contactEditText = findViewById(R.id.dateEditText);
        addButton = findViewById(R.id.addButton);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Add button click listener
        addButton.setOnClickListener(v -> addGuest());
    }

    private void addGuest() {
        String name = nameEditText.getText().toString().trim();
        String contact = contactEditText.getText().toString().trim();
    
        if (name.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please enter all details.", Toast.LENGTH_SHORT).show();
            return;
        }
    
        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(contact).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }
    
        // Get current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();
    
        // Create a new Guest object with createdBy field
        Guest newGuest = new Guest(name, contact, userId);
    
        db.collection("guests")
                .add(newGuest)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Guest added successfully.", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity and return to previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding guest: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}