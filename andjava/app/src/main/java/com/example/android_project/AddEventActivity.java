package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText titleEditText, dateEditText, timeEditText, placeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event2);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Find views
        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        placeEditText = findViewById(R.id.placeEditText);
        Button addButton = findViewById(R.id.addButton);

        // Set click listener for the "Add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String place = placeEditText.getText().toString();
                createEvent(title, date, time, place);
            }
        });
    }

    private void createEvent(String title, String date, String time, String place) {
        // Create a new event document in Firestore
        DocumentReference newEventRef = db.collection("events").document();
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("title", title);
        eventData.put("date", date);
        eventData.put("time", time);
        eventData.put("place", place);
        eventData.put("createdBy", mAuth.getCurrentUser().getUid());

        newEventRef.set(eventData)
                .addOnSuccessListener(aVoid -> {
                    // Event creation successful
                    Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT).show();
                    // Reset the form or navigate to another screen
                    Intent intent = new Intent(AddEventActivity.this,EventsListActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Event creation failed
                    Toast.makeText(this, "Error creating event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}