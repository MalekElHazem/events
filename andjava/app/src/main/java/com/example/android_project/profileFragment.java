package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText nameEditText, emailEditText;
    private Button saveProfileButton, changePasswordButton;

    @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_profile, container, false);

    ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });

    // Initialize Firebase Auth and Firestore
    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    // Find views
    nameEditText = view.findViewById(R.id.nameEditText);
    emailEditText = view.findViewById(R.id.emailEditText);
    saveProfileButton = view.findViewById(R.id.saveProfileButton);
    changePasswordButton = view.findViewById(R.id.changePasswordButton);

    // Load current user's profile
    loadUserProfile();

    // Set click listeners
    saveProfileButton.setOnClickListener(v -> {
        updateProfile();
        Intent intent = new Intent(requireActivity(), Nav.class);
        startActivity(intent);
    });

    changePasswordButton.setOnClickListener(v -> {
        // Navigate to ChangePasswordActivity
        Intent intent = new Intent(requireActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    });

    return view;
}

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Set name and email in EditText fields
            nameEditText.setText(currentUser.getDisplayName());
            emailEditText.setText(currentUser.getEmail());
        }
    }
    
    private void updateProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String newName = nameEditText.getText().toString();
            String newEmail = emailEditText.getText().toString();
    
            // Update Firebase user profile
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();
    
            currentUser.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                if (profileTask.isSuccessful()) {
                    // Update user email
                    currentUser.updateEmail(newEmail).addOnCompleteListener(emailTask -> {
                        if (emailTask.isSuccessful()) {
                            // Update Firestore user document
                            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("name", newName);
                            updates.put("email", newEmail);
    
                            userRef.update(updates).addOnCompleteListener(firestoreTask -> {
                                if (firestoreTask.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Failed to update Firestore.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(requireContext(), "Failed to update email.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*private EditText nameEditText, emailEditText, genderEditText, passwordEditText;
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
    }*/
}
