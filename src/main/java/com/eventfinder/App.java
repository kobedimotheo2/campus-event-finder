package com.eventfinder;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;

public class App extends Application {

    private ObservableList<Event> events = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        events.addAll(FileManager.loadEvents());

        // --- HEADER ---
        Label title = new Label("🎓 UB Campus Event Finder");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        HBox header = new HBox(title);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setStyle("-fx-background-color: #1e1e2e;");

        // --- FORM FIELDS ---
        TextField titleField = new TextField();
        titleField.setPromptText("e.g. Career Fair");
        styleTextField(titleField);

        TextField descField = new TextField();
        descField.setPromptText("e.g. Meet top employers");
        styleTextField(descField);

        TextField locationField = new TextField();
        locationField.setPromptText("e.g. Main Hall");
        styleTextField(locationField);

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Academic", "Sports", "Social", "Career", "Other");
        categoryBox.setPromptText("Select category");
        categoryBox.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white; -fx-border-color: #44475a; -fx-border-radius: 6; -fx-background-radius: 6;");
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white;");
        datePicker.setMaxWidth(Double.MAX_VALUE);

        // --- FILTERED LIST ---
        FilteredList<Event> filteredEvents = new FilteredList<>(events, p -> true);

        // --- SEARCH FIELD ---
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search events...");
        styleTextField(searchField);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEvents.setPredicate(event -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String search = newValue.toLowerCase();
                return event.getTitle().toLowerCase().contains(search)
                    || event.getCategory().toLowerCase().contains(search)
                    || event.getLocation().toLowerCase().contains(search);
            });
        });

        // --- CATEGORY FILTER ---
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Academic", "Sports", "Social", "Career", "Other");
        filterBox.setValue("All");
        filterBox.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white; -fx-border-color: #44475a; -fx-border-radius: 6; -fx-background-radius: 6;");
        filterBox.setOnAction(e -> {
            String selected = filterBox.getValue();
            filteredEvents.setPredicate(event -> {
                if (selected.equals("All")) return true;
                return event.getCategory().equals(selected);
            });
        });

        Button addButton = new Button("+ Add Event");
        addButton.setStyle("-fx-background-color: #6272a4; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        addButton.setMaxWidth(Double.MAX_VALUE);

        // --- FORM LAYOUT ---
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color: #282a36; -fx-background-radius: 12;");

        form.add(makeLabel("Title"), 0, 0);
        form.add(titleField, 1, 0);
        form.add(makeLabel("Description"), 0, 1);
        form.add(descField, 1, 1);
        form.add(makeLabel("Location"), 0, 2);
        form.add(locationField, 1, 2);
        form.add(makeLabel("Category"), 0, 3);
        form.add(categoryBox, 1, 3);
        form.add(makeLabel("Date"), 0, 4);
        form.add(datePicker, 1, 4);
        form.add(makeLabel("Search"), 0, 5);
        form.add(searchField, 1, 5);
        form.add(addButton, 1, 6);

        ColumnConstraints col1 = new ColumnConstraints(110);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(col1, col2);

        // --- TABLE ---
        TableView<Event> table = new TableView<>();
        table.setStyle("-fx-background-color: #282a36; -fx-text-fill: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Event, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Event, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Event, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Event, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Event, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().addAll(dateCol, titleCol, catCol, locationCol, descCol);
        table.setItems(filteredEvents);

        // --- DELETE BUTTON ---
        Button deleteButton = new Button("🗑 Delete Selected");
        deleteButton.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-cursor: hand;");
        deleteButton.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                events.remove(selected);
                FileManager.saveEvents(events);
            } else {
                showAlert("Please select an event to delete.");
            }
        });

        // --- FILTER BAR ---
        HBox filterBar = new HBox(10, makeLabel("Filter by category:"), filterBox);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(0, 0, 0, 5));

        // --- BOTTOM BAR ---
        HBox bottomBar = new HBox(20, filterBar, deleteButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(15, 25, 15, 25));
        bottomBar.setStyle("-fx-background-color: #1e1e2e;");

        // --- ADD BUTTON LOGIC ---
        addButton.setOnAction(e -> {
            String eventTitle = titleField.getText();
            String desc = descField.getText();
            String location = locationField.getText();
            String category = categoryBox.getValue();
            LocalDate date = datePicker.getValue();

            if (eventTitle.isEmpty() || desc.isEmpty() || location.isEmpty()
                    || category == null || date == null) {
                showAlert("Please fill in all fields.");
                return;
            }

            events.add(new Event(eventTitle, desc, category, location, date));
            FileManager.saveEvents(events);

            titleField.clear();
            descField.clear();
            locationField.clear();
            categoryBox.setValue(null);
            datePicker.setValue(null);
        });

        // --- MAIN LAYOUT ---
        VBox root = new VBox(header, form, table, bottomBar);
        root.setStyle("-fx-background-color: #1e1e2e;");

        Scene scene = new Scene(root, 750, 700);
        stage.setTitle("UB Campus Event Finder");
        stage.setScene(scene);
        stage.show();
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#bd93f9"));
        l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        return l;
    }

    private void styleTextField(TextField field) {
        field.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white; -fx-prompt-text-fill: #6272a4; -fx-border-color: #44475a; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8;");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}