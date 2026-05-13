# 🎓 UB Campus Event Finder

A desktop application built with Java and JavaFX that helps University of Botswana students discover and manage campus events.

## Features

### 👤 User Roles
- **Admin Mode** — full access with a passcode (default: `UB2026`)
- **View Only Mode** — browse and search events without editing

### 📋 Admin Features
- Add events with title, description, location, category and date
- Upload an image for each event
- Edit existing events
- Delete events

### 👁 View Only Features
- Search events by title, category or location
- Click any event to view full details

### 🎨 Event Detail View
- Full event info with a beautiful image banner
- Gradient fade effect on images
- Countdown showing how many days until the event
- 📤 **Share** — copies event details to clipboard for WhatsApp
- 🗺 **Open in Maps** — opens the location in Google Maps

### 📊 Smart Features
- Events automatically sorted by date
- Color coded dates — 🟠 within 7 days, ⬤ past events
- Startup notification showing upcoming events this week
- Filter events by category
- Data saves automatically and persists after closing

## Technologies Used

- Java 24
- JavaFX 21
- Maven
- File I/O for data persistence

## How to Run

### Prerequisites
- Java JDK 8 or higher
- Maven

### Steps
1. Clone the repository
    git clone https://github.com/kobedimotheo2/campus-event-finder.git

2. navigate to project folder 

3. Run the app
   mvn javafx:run

4. Enter passcode `UB2026` for admin access or click View Only

## What I Learned

- Building desktop UIs with JavaFX
- Role based access control
- Image handling and gradient overlays
- System clipboard integration
- Opening URLs from a desktop app
- FilteredList and SortedList for dynamic data
- File I/O for data persistence
- Version control with Git and GitHub

## Author

**Motheo Kobedi**
First Year Computer Science Student — University of Botswana
GitHub: [@kobedimotheo2](https://github.com/kobedimotheo2)