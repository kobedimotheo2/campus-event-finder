package com.eventfinder;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;

public class App extends Application {

    private ObservableList<Event> events = FXCollections.observableArrayList();
    private static final String PASSCODE = "UB2026";
    private boolean isAdmin = false;
    private Stage mainStage;
    private Scene mainScene;
    private String selectedImagePath = null;

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        events.addAll(FileManager.loadEvents());

        // --- MODERN PASSCODE SCREEN ---
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.UNDECORATED);

        Label lockIcon = new Label("🔐");
        lockIcon.setFont(Font.font("Arial", 48));

        Label appName = new Label("UB Campus Event Finder");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        appName.setTextFill(Color.WHITE);

        Label subtitle = new Label("Enter passcode to manage events");
        subtitle.setFont(Font.font("Arial", 13));
        subtitle.setTextFill(Color.web("#6272a4"));

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter passcode...");
        passField.setMaxWidth(280);
        passField.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white; " +
            "-fx-prompt-text-fill: #6272a4; -fx-border-color: #44475a; " +
            "-fx-border-radius: 8; -fx-background-radius: 8; " +
            "-fx-padding: 12; -fx-font-size: 14px;");

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#ff5555"));
        errorLabel.setFont(Font.font("Arial", 12));

        Button loginBtn = new Button("🔓 Enter as Admin");
        loginBtn.setMaxWidth(280);
        loginBtn.setStyle("-fx-background-color: #bd93f9; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; " +
            "-fx-background-radius: 8; -fx-cursor: hand;");

        Button viewOnlyBtn = new Button("👁 View Only");
        viewOnlyBtn.setMaxWidth(280);
        viewOnlyBtn.setStyle("-fx-background-color: #44475a; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; " +
            "-fx-background-radius: 8; -fx-cursor: hand;");

        loginBtn.setOnAction(e -> {
            if (passField.getText().equals(PASSCODE)) {
                isAdmin = true;
                loginStage.close();
                showMainApp();
            } else {
                errorLabel.setText("❌ Wrong passcode. Try again.");
                passField.clear();
            }
        });

        passField.setOnAction(e -> loginBtn.fire());

        viewOnlyBtn.setOnAction(e -> {
            isAdmin = false;
            loginStage.close();
            showMainApp();
        });

        VBox loginBox = new VBox(15,
            lockIcon, appName, subtitle,
            new Separator(),
            passField, errorLabel,
            loginBtn, viewOnlyBtn
        );
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(40));
        loginBox.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 16;");
        loginBox.setMaxWidth(360);

        StackPane loginRoot = new StackPane(loginBox);
        loginRoot.setStyle("-fx-background-color: #1e1e2e;");

        Scene loginScene = new Scene(loginRoot, 400, 480);
        loginStage.setScene(loginScene);
        loginStage.centerOnScreen();
        loginStage.showAndWait();
    }

    private void showMainApp() {

        // --- HEADER ---
        Label title = new Label("🎓 UB Campus Event Finder");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Label modeLabel = new Label(isAdmin ? "🔓 Admin Mode" : "🔒 View Only");
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        modeLabel.setTextFill(isAdmin ? Color.web("#50fa7b") : Color.web("#ff5555"));

        HBox header = new HBox(20, title, modeLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setStyle("-fx-background-color: #1e1e2e;");

        // --- FORM FIELDS ---
        TextField titleField = new TextField();
        titleField.setPromptText("e.g. Career Fair");
        styleTextField(titleField);
        titleField.setDisable(!isAdmin);

        TextField descField = new TextField();
        descField.setPromptText("e.g. Meet top employers");
        styleTextField(descField);
        descField.setDisable(!isAdmin);

        TextField locationField = new TextField();
        locationField.setPromptText("e.g. Main Hall");
        styleTextField(locationField);
        locationField.setDisable(!isAdmin);

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Academic", "Sports", "Social", "Career", "Other");
        categoryBox.setPromptText("Select category");
        categoryBox.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white; -fx-border-color: #44475a; -fx-border-radius: 6; -fx-background-radius: 6;");
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        categoryBox.setDisable(!isAdmin);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white;");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setDisable(!isAdmin);

        // --- IMAGE PICKER ---
        Label imagePathLabel = new Label("No image selected");
        imagePathLabel.setTextFill(Color.web("#6272a4"));
        imagePathLabel.setFont(Font.font("Arial", 11));

        Button imagePickerBtn = new Button("🖼 Upload Image");
        imagePickerBtn.setStyle("-fx-background-color: #44475a; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 14; -fx-background-radius: 8; -fx-cursor: hand;");
        imagePickerBtn.setDisable(!isAdmin);
        imagePickerBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Event Image");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(mainStage);
            if (file != null) {
                selectedImagePath = file.getAbsolutePath();
                imagePathLabel.setText("✅ " + file.getName());
                imagePathLabel.setTextFill(Color.web("#50fa7b"));
            }
        });

        HBox imagePicker = new HBox(10, imagePickerBtn, imagePathLabel);
        imagePicker.setAlignment(Pos.CENTER_LEFT);

        // --- FILTERED + SORTED LIST ---
        FilteredList<Event> filteredEvents = new FilteredList<>(events, p -> true);
        SortedList<Event> sortedEvents = new SortedList<>(filteredEvents,
            Comparator.comparing(Event::getDate));

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
        addButton.setDisable(!isAdmin);

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
        form.add(makeLabel("Image"), 0, 5);
        form.add(imagePicker, 1, 5);
        form.add(makeLabel("Search"), 0, 6);
        form.add(searchField, 1, 6);
        form.add(addButton, 1, 7);

        ColumnConstraints col1 = new ColumnConstraints(110);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(col1, col2);

        // --- TABLE ---
        TableView<Event> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: #282a36;" +
            "-fx-border-color: #44475a;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(45);

        table.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<Event>() {
                @Override
                protected void updateItem(Event item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("-fx-background-color: transparent;");
                    } else if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #282a36;");
                    } else {
                        setStyle("-fx-background-color: #2a2a3e;");
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    showDetailView(row.getItem());
                }
            });

            return row;
        });

        TableColumn<Event, LocalDate> dateCol = new TableColumn<>("📅 Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(col -> new TableCell<Event, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(
                        LocalDate.now(), item);
                    if (daysUntil >= 0 && daysUntil <= 7) {
                        setStyle("-fx-text-fill: #ffb86c; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else if (daysUntil < 0) {
                        setStyle("-fx-text-fill: #6272a4; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        TableColumn<Event, String> titleCol = new TableColumn<>("🎯 Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(col -> new TableCell<Event, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            }
        });

        TableColumn<Event, String> catCol = new TableColumn<>("🏷 Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setCellFactory(col -> new TableCell<Event, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    String color = switch (item) {
                        case "Academic" -> "#8be9fd";
                        case "Sports" -> "#50fa7b";
                        case "Social" -> "#ff79c6";
                        case "Career" -> "#ffb86c";
                        default -> "#bd93f9";
                    };
                    setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<Event, String> locationCol = new TableColumn<>("📍 Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setCellFactory(col -> new TableCell<Event, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-text-fill: #f8f8f2;");
            }
        });

        table.getColumns().addAll(dateCol, titleCol, catCol, locationCol);
        table.setItems(sortedEvents);

        Label hintLabel = new Label("💡 Click an event to view details");
        hintLabel.setTextFill(Color.web("#6272a4"));
        hintLabel.setFont(Font.font("Arial", 11));

        // --- EDIT BUTTON ---
        Button editButton = new Button("✏ Edit Selected");
        editButton.setStyle("-fx-background-color: #ffb86c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-cursor: hand;");
        editButton.setDisable(!isAdmin);
        editButton.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Please select an event to edit.");
                return;
            }

            Dialog<Event> dialog = new Dialog<>();
            dialog.setTitle("Edit Event");
            dialog.setHeaderText("Update event details");

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            TextField editTitle = new TextField(selected.getTitle());
            styleTextField(editTitle);
            TextField editDesc = new TextField(selected.getDescription());
            styleTextField(editDesc);
            TextField editLocation = new TextField(selected.getLocation());
            styleTextField(editLocation);
            ComboBox<String> editCategory = new ComboBox<>();
            editCategory.getItems().addAll("Academic", "Sports", "Social", "Career", "Other");
            editCategory.setValue(selected.getCategory());
            editCategory.setStyle("-fx-background-color: #2a2a3e; -fx-text-fill: white; -fx-border-color: #44475a; -fx-border-radius: 6; -fx-background-radius: 6;");
            DatePicker editDate = new DatePicker(selected.getDate());

            GridPane editForm = new GridPane();
            editForm.setHgap(10);
            editForm.setVgap(10);
            editForm.setPadding(new Insets(20));
            editForm.setStyle("-fx-background-color: #282a36;");
            editForm.add(makeLabel("Title:"), 0, 0);
            editForm.add(editTitle, 1, 0);
            editForm.add(makeLabel("Description:"), 0, 1);
            editForm.add(editDesc, 1, 1);
            editForm.add(makeLabel("Location:"), 0, 2);
            editForm.add(editLocation, 1, 2);
            editForm.add(makeLabel("Category:"), 0, 3);
            editForm.add(editCategory, 1, 3);
            editForm.add(makeLabel("Date:"), 0, 4);
            editForm.add(editDate, 1, 4);

            dialog.getDialogPane().setContent(editForm);
            dialog.getDialogPane().setStyle("-fx-background-color: #282a36;");

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selected.setTitle(editTitle.getText());
                    selected.setDescription(editDesc.getText());
                    selected.setLocation(editLocation.getText());
                    selected.setCategory(editCategory.getValue());
                    selected.setDate(editDate.getValue());
                    return selected;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(updated -> {
                table.refresh();
                FileManager.saveEvents(events);
            });
        });

        // --- DELETE BUTTON ---
        Button deleteButton = new Button("🗑 Delete Selected");
        deleteButton.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-cursor: hand;");
        deleteButton.setDisable(!isAdmin);
        deleteButton.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                events.remove(selected);
                FileManager.saveEvents(events);
            } else {
                showAlert("Please select an event to delete.");
            }
        });

        Label upcomingLegend = new Label("🟠 Within 7 days");
        upcomingLegend.setTextFill(Color.web("#ffb86c"));
        upcomingLegend.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Label pastLegend = new Label("⬤ Past events");
        pastLegend.setTextFill(Color.web("#6272a4"));
        pastLegend.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        HBox filterBar = new HBox(10, makeLabel("Filter:"), filterBox,
            upcomingLegend, pastLegend);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        HBox bottomBar = new HBox(20, filterBar, editButton, deleteButton);
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

            events.add(new Event(eventTitle, desc, category, location, date, selectedImagePath));
            FileManager.saveEvents(events);

            titleField.clear();
            descField.clear();
            locationField.clear();
            categoryBox.setValue(null);
            datePicker.setValue(null);
            selectedImagePath = null;
            imagePathLabel.setText("No image selected");
            imagePathLabel.setTextFill(Color.web("#6272a4"));
        });

        VBox root = new VBox(header, form, table, hintLabel, bottomBar);
        root.setStyle("-fx-background-color: #1e1e2e;");
        VBox.setMargin(hintLabel, new Insets(5, 25, 0, 25));

        mainScene = new Scene(root, 750, 750);
        mainStage.setTitle("UB Campus Event Finder");
        mainStage.setScene(mainScene);
        mainStage.centerOnScreen();
        mainStage.show();
    }

    private void showDetailView(Event event) {

        String catColor = switch (event.getCategory()) {
            case "Academic" -> "#8be9fd";
            case "Sports" -> "#50fa7b";
            case "Social" -> "#ff79c6";
            case "Career" -> "#ffb86c";
            default -> "#bd93f9";
        };

        // --- BACK BUTTON ---
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-background-color: #44475a; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-cursor: hand;");
        backButton.setOnAction(e -> mainStage.setScene(mainScene));

        HBox topBar = new HBox(backButton);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #1e1e2e;");

        // --- EVENT IMAGE WITH GRADIENT FADE ---
StackPane imageSection = new StackPane();
if (event.getImagePath() != null) {
    try {
        Image img = new Image("file:///" + event.getImagePath().replace("\\", "/"));
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(750);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(false);

        // Gradient overlay that fades image into background
        Region gradientOverlay = new Region();
        gradientOverlay.setPrefWidth(750);
        gradientOverlay.setPrefHeight(250);
        gradientOverlay.setStyle(
            "-fx-background-color: linear-gradient(" +
            "from 0% 0% to 0% 100%, " +
            "transparent 0%, " +
            "transparent 40%, " +
            "rgba(30,30,46,0.6) 70%, " +
            "rgba(30,30,46,1.0) 100%" +
            ");"
        );

        imageSection.getChildren().addAll(imageView, gradientOverlay);

    } catch (Exception e) {
        // image failed to load skip
    }
}

        // --- TITLE SECTION ---
        Label eventIcon = new Label("📌");
        eventIcon.setFont(Font.font("Arial", 48));

        Label eventTitle = new Label(event.getTitle());
        eventTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        eventTitle.setTextFill(Color.WHITE);
        eventTitle.setWrapText(true);

        Label categoryBadge = new Label("  " + event.getCategory() + "  ");
        categoryBadge.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        categoryBadge.setTextFill(Color.web(catColor));
        categoryBadge.setStyle("-fx-background-color: #282a36; -fx-background-radius: 12; -fx-padding: 4 10;");

        VBox titleSection = new VBox(10, eventIcon, eventTitle, categoryBadge);
        titleSection.setPadding(new Insets(20, 30, 20, 30));
        titleSection.setStyle("-fx-background-color: #1e1e2e;");

        // --- COUNTDOWN ---
        long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(
            LocalDate.now(), event.getDate());

        String countdownText;
        String countdownColor;
        if (daysUntil < 0) {
            countdownText = "This event has passed";
            countdownColor = "#6272a4";
        } else if (daysUntil == 0) {
            countdownText = "🔥 This event is TODAY!";
            countdownColor = "#ff5555";
        } else if (daysUntil <= 7) {
            countdownText = "⚡ Coming up in " + daysUntil + " day(s)!";
            countdownColor = "#ffb86c";
        } else {
            countdownText = "📆 " + daysUntil + " days away";
            countdownColor = "#50fa7b";
        }

        Label countdown = new Label(countdownText);
        countdown.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        countdown.setTextFill(Color.web(countdownColor));
        countdown.setPadding(new Insets(5, 30, 5, 30));

        // --- DETAIL CARDS ---
        VBox detailBox = new VBox(15,
            detailCard("📅 Date", event.getDate().toString()),
            detailCard("📍 Location", event.getLocation()),
            detailCard("📝 Description", event.getDescription())
        );
        detailBox.setPadding(new Insets(20, 30, 30, 30));
        detailBox.setStyle("-fx-background-color: #1e1e2e;");

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(topBar, imageSection, titleSection, countdown, detailBox);
        content.setStyle("-fx-background-color: #1e1e2e;");
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1e1e2e; -fx-background: #1e1e2e;");

        Scene detailScene = new Scene(scrollPane, 750, 700);
        mainStage.setScene(detailScene);
    }

    private VBox detailCard(String label, String value) {
        Label cardLabel = new Label(label);
        cardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        cardLabel.setTextFill(Color.web("#bd93f9"));

        Label cardValue = new Label(value);
        cardValue.setFont(Font.font("Arial", 15));
        cardValue.setTextFill(Color.WHITE);
        cardValue.setWrapText(true);

        VBox card = new VBox(5, cardLabel, cardValue);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setStyle("-fx-background-color: #282a36; -fx-background-radius: 10;");

        return card;
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