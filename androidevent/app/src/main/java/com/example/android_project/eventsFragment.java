package com.example.android_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class eventsFragment extends Fragment {

    private LinearLayout eventContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_events, container, false);
        eventContainer = view.findViewById(R.id.eventContainer);

        // Add event button click listener
        ImageButton addButton = view.findViewById(R.id.addEventButton);
        addButton.setOnClickListener(v -> {
            // Handle add event click
        });

        populateEvents();
        return view;
    }

    private void populateEvents() {
        Event2[] events = {
                new Event2("Team Meeting", "Today, 2:00 PM", R.drawable.gnx),
                new Event2("Birthday Party", "Tomorrow, 6:00 PM", R.drawable.mrmorale),
                new Event2("Conference", "Next Week", R.drawable.tbap),
                new Event2("Conference", "Next Week", R.drawable.gkmc)

        };

        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Event2 event : events) {
            View eventCard = inflater.inflate(R.layout.event_cards, eventContainer, false);

            TextView titleView = eventCard.findViewById(R.id.eventTitle);
            TextView dateTimeView = eventCard.findViewById(R.id.eventDateTime);
            ImageView imageView = eventCard.findViewById(R.id.eventImage);
            ImageButton editButton = eventCard.findViewById(R.id.editButton);
            ImageButton deleteButton = eventCard.findViewById(R.id.deleteButton);

            titleView.setText(event.getTitle());
            dateTimeView.setText(event.getDateTime());
            imageView.setImageResource(event.getImageResourceId());

            editButton.setOnClickListener(v -> {
                // Handle edit event
            });

            deleteButton.setOnClickListener(v -> {
                eventContainer.removeView(eventCard);
            });

            eventContainer.addView(eventCard);
        }
    }
}
