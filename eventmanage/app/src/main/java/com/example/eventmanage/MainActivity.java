// MainActivity.java
package com.example.eventmanage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout eventContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        eventContainer = findViewById(R.id.eventContainer); // Add this ID to your LinearLayout
        populateEvents();
    }

    private void populateEvents() {
        // Sample events
        Event[] events = {
                new Event("Birthday Party", "July 15, 2024 - 6:00 PM", R.drawable.gnx),
                new Event("Tech Conference", "August 20, 2024 - 9:00 AM", R.drawable.gnx),
                new Event("Wedding Anniversary", "September 5, 2024 - 7:30 PM", R.drawable.gnx),
                new Event("Team Meeting", "October 10, 2024 - 2:00 PM", R.drawable.gnx)
        };

        LayoutInflater inflater = LayoutInflater.from(this);

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
                // Handle edit click
            });

            deleteButton.setOnClickListener(v -> {
                // Handle delete click
                eventContainer.removeView(eventCard);
            });

            eventContainer.addView(eventCard);
        }
    }
}