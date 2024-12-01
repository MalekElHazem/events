package com.example.eventmanage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


// SettingsFragment.java
public class settingsFragment extends Fragment {
    private EditText nameEditText, emailEditText, genderEditText, passwordEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        
        // Initialize views
        nameEditText = view.findViewById(R.id.titleEditText);
        emailEditText = view.findViewById(R.id.dateEditText);
        genderEditText = view.findViewById(R.id.timeEditText);
        passwordEditText = view.findViewById(R.id.placeEditText);
        saveButton = view.findViewById(R.id.addButton);

        // Load existing profile data if any
        loadProfileData();

        // Set up save button click listener
        saveButton.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void loadProfileData() {
        // Get shared preferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        
        // Load saved data
        nameEditText.setText(prefs.getString("name", ""));
        emailEditText.setText(prefs.getString("email", ""));
        genderEditText.setText(prefs.getString("gender", ""));
        passwordEditText.setText(prefs.getString("password", ""));
    }

    private void saveProfileData() {
        // Get shared preferences editor
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE).edit();
        
        // Save data
        editor.putString("name", nameEditText.getText().toString());
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("gender", genderEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.apply();

        // Show success message
        Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }
}