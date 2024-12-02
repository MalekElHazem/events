public class Event {
    private String title;
    private String date;
    private String time;
    private String place;
    private String eventId;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String title, String date, String time, String place, String eventId) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.place = place;
        this.eventId = eventId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
