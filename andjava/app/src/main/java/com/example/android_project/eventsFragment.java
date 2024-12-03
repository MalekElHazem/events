package com.example.android_project;

import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class eventsFragment extends Fragment {

    private LinearLayout eventContainer;
    private FirebaseFirestore db;
    private List<Event> eventList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_events, container, false);
        eventContainer = view.findViewById(R.id.eventContainer);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();

        // Use MaterialButton instead of ImageButton
        MaterialButton addButton = view.findViewById(R.id.addEventButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEventActivity.class);
            startActivity(intent);
        });

        fetchEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchEvents();
    }

    private void fetchEvents() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        db.collection("events")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        eventList.clear();
                        for (DocumentSnapshot document : querySnapshot) {
                            Event event = document.toObject(Event.class);
                            event.setEventId(document.getId());
                            eventList.add(event);
                        }
                        populateEvents();
                    } else {
                        Toast.makeText(getContext(), "No events found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error fetching events: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                });
    }

    private void populateEvents() {
        eventContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Event event : eventList) {
            View eventCard = inflater.inflate(R.layout.event_card, eventContainer, false);

            TextView titleView = eventCard.findViewById(R.id.eventTitle);
            TextView dateView = eventCard.findViewById(R.id.eventDate);
            // Use MaterialButton instead of ImageButton
            MaterialButton editButton = eventCard.findViewById(R.id.editButton);
            MaterialButton deleteButton = eventCard.findViewById(R.id.deleteButton);

            titleView.setText(event.getTitle());
            dateView.setText(event.getDate());

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditEventActivity.class);
                intent.putExtra("eventId", event.getEventId());
                intent.putExtra("title", event.getTitle());
                intent.putExtra("date", event.getDate());
                startActivity(intent);
            });

            deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.collection("events")
                            .document(event.getEventId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Event deleted.", 
                                    Toast.LENGTH_SHORT).show();
                                eventList.remove(event);
                                eventContainer.removeView(eventCard);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error deleting event: " + 
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    })
                    .setNegativeButton("No", null)
                    .show();
            });

            eventContainer.addView(eventCard);
        }
    }
}