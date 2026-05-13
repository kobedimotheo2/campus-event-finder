package com.eventfinder;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FILE_NAME = "events.txt";

    public static void saveEvents(List<Event> events) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Event e : events) {
                writer.write(e.getTitle() + "," + e.getDescription() + "," 
                    + e.getCategory() + "," + e.getLocation() + "," + e.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    public static List<Event> loadEvents() {
        List<Event> events = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return events;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String title = parts[0];
                    String description = parts[1];
                    String category = parts[2];
                    String location = parts[3];
                    LocalDate date = LocalDate.parse(parts[4]);
                    events.add(new Event(title, description, category, location, date));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }

        return events;
    }
}