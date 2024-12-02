package com.example.android_project;

public class Guest {
    private String name;
    private String contact;
    private String guestId;
    private String createdBy;

    // Default constructor for Firebase
    public Guest() {
    }

    public Guest(String name, String contact, String guestId, String createdBy) {
        this.name = name;
        this.contact = contact;
        this.guestId = guestId;
        this.createdBy = createdBy;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}