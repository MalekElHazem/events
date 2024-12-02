package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class eventsFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(eventAdapter);

        ImageButton addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEventActivity.class);
            startActivity(intent);
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchEvents();
    }

    private void fetchEvents() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null || !isAdded()) return;

        db.collection("events")
                .whereEqualTo("createdBy", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!isAdded()) return;
                    
                    eventList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Event event = documentSnapshot.toObject(Event.class);
                            if (event != null) {
                                event.setEventId(documentSnapshot.getId());
                                eventList.add(event);
                            }
                        }
                    }
                    eventAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error fetching events: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onEditClick(String eventId) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), EditEventActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(String eventId) {
        if (!isAdded()) return;
        
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Event")
            .setMessage("Are you sure you want to delete this event?")
            .setPositiveButton("Yes", (dialog, which) -> {
                deleteEvent(eventId);
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void deleteEvent(String eventId) {
        db.collection("events").document(eventId).delete()
            .addOnSuccessListener(aVoid -> {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                    fetchEvents();
                }
            })
            .addOnFailureListener(e -> {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Error deleting event: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchEvents();
    }
}