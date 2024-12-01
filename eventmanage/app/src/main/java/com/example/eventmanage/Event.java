// Event.java
package com.example.eventmanage;

public class Event {
    private String title;
    private String dateTime;
    private int imageResourceId;

    public Event(String title, String dateTime, int imageResourceId) {
        this.title = title;
        this.dateTime = dateTime;
        this.imageResourceId = imageResourceId;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDateTime() { return dateTime; }
    public int getImageResourceId() { return imageResourceId; }
}