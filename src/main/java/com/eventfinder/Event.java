package com.eventfinder;

import java.time.LocalDate;

public class Event {

    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDate date;

    // Constructor
    public Event(String title, String description, String category, String location, LocalDate date) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return date + " | " + category + " | " + title + " @ " + location;
    }
}