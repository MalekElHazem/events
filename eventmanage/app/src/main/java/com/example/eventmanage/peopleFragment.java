package com.example.eventmanage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link peopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class peopleFragment extends Fragment {

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
        Event[] events = {
                new Event("Team Meeting", "Today, 2:00 PM", R.drawable.gnx),
                new Event("Birthday Party", "Tomorrow, 6:00 PM", R.drawable.gnx),
                new Event("Conference", "Next Week", R.drawable.gnx)
        };

        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Event event : events) {
            View eventCard = inflater.inflate(R.layout.event_card, eventContainer, false);

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