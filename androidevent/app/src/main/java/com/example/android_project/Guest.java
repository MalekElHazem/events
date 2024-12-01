package com.example.android_project;

// Guest.java
public class Guest {
    private String name;
    private String contact;
    private int imageResourceId;

    public Guest(String name, String contact, int imageResourceId) {
        this.name = name;
        this.contact = contact;
        this.imageResourceId = imageResourceId;
    }

    // Getters
    public String getName() { return name; }
    public String getContact() { return contact; }
    public int getImageResourceId() { return imageResourceId; }
}