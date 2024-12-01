package com.example.android_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class guestsFragment extends Fragment {

    private LinearLayout guestContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guests, container, false);
        guestContainer = view.findViewById(R.id.eventContainer);

        // Add button click listener
        ImageButton addButton = view.findViewById(R.id.addGuestButton);
        addButton.setOnClickListener(v -> {
            // Handle add guest
        });

        populateGuests();
        return view;
    }

    private void populateGuests() {
        // Sample guests
        Guest[] guests = {
                new Guest("John Doe", "+1 234-567-8900", R.drawable.side_nav_bar),
                new Guest("Jane Smith", "+1 234-567-8901", R.drawable.side_nav_bar),
                new Guest("Mike Johnson", "+1 234-567-8902", R.drawable.side_nav_bar),
                new Guest("Sarah Williams", "+1 234-567-8903", R.drawable.side_nav_bar),
        };

        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Guest guest : guests) {
            View guestCard = inflater.inflate(R.layout.guest_card, guestContainer, false);

            TextView nameView = guestCard.findViewById(R.id.eventTitle);
            ImageButton editButton = guestCard.findViewById(R.id.editButton);
            ImageButton deleteButton = guestCard.findViewById(R.id.deleteButton);

            nameView.setText(guest.getName());

            editButton.setOnClickListener(v -> {
                // Handle edit
            });

            deleteButton.setOnClickListener(v -> {
                guestContainer.removeView(guestCard);
            });

            guestContainer.addView(guestCard);
        }
    }
}
