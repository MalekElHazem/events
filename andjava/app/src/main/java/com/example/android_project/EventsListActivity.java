package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity implements EventAdapter.OnEventClickListener {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button eventsButton, guestsButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        // Initialize navigation buttons
        eventsButton = findViewById(R.id.eventsButton);
        guestsButton = findViewById(R.id.guestsButton);
        profileButton = findViewById(R.id.profileButton);

        // Set up navigation click listeners
        eventsButton.setOnClickListener(v -> navigateToEvents());
        guestsButton.setOnClickListener(v -> navigateToGuests());
        profileButton.setOnClickListener(v -> navigateToProfile());

        // Existing initialization code...
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(eventAdapter);

        // Fetch events
        fetchEvents();


        Button addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(v -> {
            // Navigate to the Add Event activity
            Intent intent = new Intent(EventsListActivity.this, AddEventActivity.class);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        fetchEvents();
    }

    private void navigateToEvents() {
        // Already on events screen
        return;
    }

    private void navigateToGuests() {
        Intent intent = new Intent(this, GuestsListActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void fetchEvents() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("events")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        eventList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Event event = documentSnapshot.toObject(Event.class);
                            event.setEventId(documentSnapshot.getId());
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching events: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onEditClick(String eventId) {
        // Handle event edit action
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(String eventId) {
        // Handle event delete action
        db.collection("events").document(eventId).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
                    fetchEvents();  // Refresh the list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
