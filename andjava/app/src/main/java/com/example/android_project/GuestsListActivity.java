package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class GuestsListActivity extends AppCompatActivity implements GuestAdapter.OnGuestClickListener {

    private RecyclerView recyclerView;
    private GuestAdapter guestAdapter;
    private List<Guest> guestList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button eventsButton, guestsButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests_list);

        // Initialize navigation buttons first
        eventsButton = findViewById(R.id.eventsButton);
        guestsButton = findViewById(R.id.guestsButton);
        profileButton = findViewById(R.id.profileButton);

        // Set up navigation click listeners
        eventsButton.setOnClickListener(v -> navigateToEvents());
        guestsButton.setOnClickListener(v -> navigateToGuests());
        profileButton.setOnClickListener(v -> navigateToProfile());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        guestList = new ArrayList<>();
        guestAdapter = new GuestAdapter(guestList, this);
        recyclerView.setAdapter(guestAdapter);

        // Fetch guests and set up refresh
        fetchGuests();


        // Set up add button
        Button addGuestButton = findViewById(R.id.addGuestButton);
        addGuestButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuestsListActivity.this, AddGuestActivity.class);
            startActivity(intent);
        });
    }

    private void fetchGuests() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("guests")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        guestList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Guest guest = documentSnapshot.toObject(Guest.class);
                            guest.setGuestId(documentSnapshot.getId());
                            guestList.add(guest);
                        }
                        guestAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching guests: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToEvents() {
        startActivity(new Intent(this, EventsListActivity.class));
        finish();
    }

    private void navigateToGuests() {
        // Already on guests screen
    }

    private void navigateToProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchGuests();
    }

    @Override
    public void onEditClick(String guestId) {
        Intent intent = new Intent(this, EditGuestActivity.class);
        intent.putExtra("guestId", guestId);
        startActivity(intent);
    }

    // GuestsListActivity.java updates
    @Override
    public void onDeleteClick(String guestId) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Guest")
            .setMessage("Are you sure you want to delete this guest?")
            .setPositiveButton("Yes", (dialog, which) -> {
                db.collection("guests").document(guestId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Guest deleted successfully", Toast.LENGTH_SHORT).show();
                        fetchGuests();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error deleting guest: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    });
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void deleteGuest(String guestId) {
        db.collection("guests").document(guestId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Guest deleted", Toast.LENGTH_SHORT).show();
                    fetchGuests();
                })
                .addOnFailureListener(e -> 
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}