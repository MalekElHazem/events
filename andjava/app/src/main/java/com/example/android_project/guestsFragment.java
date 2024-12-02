package com.example.android_project;

import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class guestsFragment extends Fragment {

    private LinearLayout guestContainer;
    private FirebaseFirestore db;
    private List<Guest> guestList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guests, container, false);
        guestContainer = view.findViewById(R.id.guestContainer);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        guestList = new ArrayList<>();

        // Add button click listener
        ImageButton addButton = view.findViewById(R.id.addGuestButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddGuestActivity.class);
            startActivity(intent);
        });

        // Fetch and display guests
        fetchGuests();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchGuests(); // Refresh the guest list when the fragment resumes
    }

    private void fetchGuests() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();
    
        // Query guests where createdBy equals current user's ID
        db.collection("guests")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        guestList.clear();
                        for (DocumentSnapshot document : querySnapshot) {
                            Guest guest = document.toObject(Guest.class);
                            guest.setGuestId(document.getId()); // Store document ID
                            guestList.add(guest);
                        }
                        populateGuests();
                    } else {
                        Toast.makeText(getContext(), "No guests found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error fetching guests: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void populateGuests() {
        guestContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        // Inside populateGuests() method
        for (Guest guest : guestList) {
            View guestCard = inflater.inflate(R.layout.guest_card, guestContainer, false);

            TextView nameView = guestCard.findViewById(R.id.guestName);
            TextView contactView = guestCard.findViewById(R.id.guestContact);
            ImageButton editButton = guestCard.findViewById(R.id.editButton);
            ImageButton deleteButton = guestCard.findViewById(R.id.deleteButton);

            nameView.setText(guest.getName());
            contactView.setText(guest.getContact());

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditGuestActivity.class);
                intent.putExtra("guestId", guest.getGuestId());
                intent.putExtra("name", guest.getName());
                intent.putExtra("contact", guest.getContact());
                startActivity(intent);
            });

            deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Guest")
                    .setMessage("Are you sure you want to delete this guest?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.collection("guests")
                            .document(guest.getGuestId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Guest deleted.", Toast.LENGTH_SHORT).show();
                                guestList.remove(guest);
                                guestContainer.removeView(guestCard);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error deleting guest: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    })
                    .setNegativeButton("No", null)
                    .show();
            });

            guestContainer.addView(guestCard);
        }
    }
}