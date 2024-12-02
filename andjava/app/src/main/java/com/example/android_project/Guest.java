package com.example.android_project;

public class Guest {
    private String name;
    private String contact;
    private String id; // Firestore document ID
    private String createdBy; // User ID or Email

    public Guest() {
        // Required empty constructor for Firestore deserialization
    }

    public Guest(String name, String contact, String createdBy) {
        this.name = name;
        this.contact = contact;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getId() { return id; }
    public String getCreatedBy() { return createdBy; }

    public void setName(String name) { this.name = name; }
    public void setContact(String contact) { this.contact = contact; }
    public void setId(String id) { this.id = id; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}