package com.eventfinder;

import java.time.LocalDate;

public class Event {

    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDate date;
    private String imagePath;

    // Constructor
    public Event(String title, String description, String category, String location, LocalDate date, String imagePath) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
        this.imagePath = imagePath;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }
    public String getImagePath() { return imagePath; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return date + " | " + category + " | " + title + " @ " + location;
    }
}