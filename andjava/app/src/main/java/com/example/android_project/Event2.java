package com.example.android_project;

// Event.java
public class Event2 {
    private String title;
    private String dateTime;
    private int imageResourceId;

    public Event2(String title, String dateTime, int imageResourceId) {
        this.title = title;
        this.dateTime = dateTime;
        this.imageResourceId = imageResourceId;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDateTime() { return dateTime; }
    public int getImageResourceId() { return imageResourceId; }
}