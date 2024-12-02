package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpeningScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opening_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.openingscreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize the MyEvent logo or any other UI elements on this screen
        ImageView myEventLogo = findViewById(R.id.imageView4);

        // Add any necessary event listeners or navigation logic here
        // For example, you could add a click listener to the logo to navigate to the next screen
        myEventLogo.setOnClickListener(v -> navigateToWelcomeScreen());
    }

    private void navigateToWelcomeScreen() {
        // Create an intent to navigate to the Welcome screen
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        startActivity(intent);
    }
}