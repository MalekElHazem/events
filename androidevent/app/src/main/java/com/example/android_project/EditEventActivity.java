package com.example.android_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditEventActivity extends AppCompatActivity {

    private EditText titleEditText, dateEditText, timeEditText, placeEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind views
        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        placeEditText = findViewById(R.id.placeEditText);
        saveButton = findViewById(R.id.addButton);

        // Get data from intent
        eventId = getIntent().getStringExtra("eventId");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String place = getIntent().getStringExtra("place");

        // Populate fields
        titleEditText.setText(title);
        dateEditText.setText(date);
        timeEditText.setText(time);
        placeEditText.setText(place);

        // Handle save button
        saveButton.setText("Update");
        saveButton.setOnClickListener(v -> updateEvent());
    }

    private void updateEvent() {
        String title = titleEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String place = placeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(place)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update Firestore
        db.collection("events").document(eventId)
                .update("title", title, "date", date, "time", time, "place", place)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to previous activity
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating event: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
