package com.example.android_project;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditGuestActivity extends AppCompatActivity {

    private EditText nameEditText, contactEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private String guestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_guest);

        // Initialize views
        nameEditText = findViewById(R.id.titleEditText);
        contactEditText = findViewById(R.id.dateEditText);
        saveButton = findViewById(R.id.addButton);
        db = FirebaseFirestore.getInstance();

        // Get guest data from intent
        guestId = getIntent().getStringExtra("guestId");
        String name = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contact");

        // Set current values
        nameEditText.setText(name);
        contactEditText.setText(contact);

        // Save button click listener
        saveButton.setOnClickListener(v -> updateGuest());
    }

    private void updateGuest() {
        String name = nameEditText.getText().toString().trim();
        String contact = contactEditText.getText().toString().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(contact).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update guest in Firestore
        db.collection("guests").document(guestId)
                .update("name", name, "contact", contact)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Guest updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating guest: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}