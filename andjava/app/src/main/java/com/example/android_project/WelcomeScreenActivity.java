package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcomeScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView welcomeText = findViewById(R.id.welcomeText);
        ImageView eventPlanningIcon = findViewById(R.id.eventPlanningIcon);

        // Add a click listener to the navigation arrow to move to the next screen
        ImageView navigationArrow = findViewById(R.id.button);
        navigationArrow.setOnClickListener(v -> navigateToLoginScreen());
    }

    private void navigateToLoginScreen() {
        // Create an intent to navigate to the Login screen
        Intent intent = new Intent(this,loginActivity.class);

        // Clear the back stack to prevent navigating back to the welcome screen
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Optional: Close the current activity after navigating
    }

}
