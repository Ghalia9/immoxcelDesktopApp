package tn.esprit.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import tn.esprit.models.GoogleCalendarAuth;
import tn.esprit.models.GoogleCalendarAuth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class GoogleCalendarEventsController {

    @FXML
    private CalendarView calendarView; // New CalendarFX component

    public void initialize() throws GeneralSecurityException, IOException {
        // Keep your existing logic for ListView
        fetchGoogleCalendarEventsAndUpdateListView();
        // Initialize and configure CalendarFX
        initializeCalendarFX();
        // Start the event update timer
        //startEventUpdateTimer();
    }

    private void fetchGoogleCalendarEventsAndUpdateListView() throws IOException, GeneralSecurityException {
        // Your existing logic to fetch events and update ListView
        final List<Event> events = fetchGoogleCalendarEvents2();
    }

    static GoogleCalendarAuth Auth = new GoogleCalendarAuth();

    private List<Event> fetchGoogleCalendarEvents2() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        System.out.println("Here");
        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, Auth.getJSON_FACTORY(), getCredentials(HTTP_TRANSPORT))
                .setApplicationName(Auth.getAPPLICATION_NAME())
                .build();

        Events events = service.events().list("63f3800bafaea2d61e97715d8096a30c7ee0918b3b3238e61d6baabe02fa1b67@group.calendar.google.com").execute();
        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events:");
        }
        return items;
    }

    private void initializeCalendarFX() {
        try {
            // Fetch events from Google Calendar
            final List<Event> events = fetchGoogleCalendarEvents2();

            // Create a calendar source
            CalendarSource calendarSource = new CalendarSource("Google Calendar");


            // Create a calendar
            Calendar calendar = new Calendar("Google Calendar");
            calendar.setStyle(Calendar.Style.STYLE3);

            // Set a custom entry details callback
            calendarView.setEntryDetailsCallback(param -> {
                // Create your custom entry dialog content
                Node customContent = createCustomEntryDialogContent(param);

                // Create a dialog to display the custom content
                Alert dialog = new Alert(Alert.AlertType.NONE);
                dialog.setTitle("Custom Entry Details");
                dialog.getDialogPane().setContent(customContent);

                // Show the dialog
                dialog.showAndWait();

                // Return null because the dialog handles the display of entry details
                return null;
            });

// Add entries to the calendar
            // Add entries to the calendar
            for (Event event : events) {
                String summary = event.getSummary();
                // Ensure summary is not null
                if (summary != null) {
                    // Initialize start and end date/time variables
                    LocalDateTime startDateTime = null;
                    LocalDateTime endDateTime = null;

                    // Check if start and end dates have been set with time
                    if (event.getStart() != null && event.getStart().getDateTime() != null) {
                        startDateTime = ZonedDateTime.parse(event.getStart().getDateTime().toString()).toLocalDateTime();
                    }
                    if (event.getEnd() != null && event.getEnd().getDateTime() != null) {
                        endDateTime = ZonedDateTime.parse(event.getEnd().getDateTime().toString()).toLocalDateTime();
                    }

                    // Check if start and end dates have been set without time (all-day event)
                    if (event.getStart() != null && event.getStart().getDate() != null) {
                        LocalDate startDate = LocalDate.parse(event.getStart().getDate().toString());
                        startDateTime = startDate.atStartOfDay();
                    }
                    if (event.getEnd() != null && event.getEnd().getDate() != null) {
                        LocalDate endDate = LocalDate.parse(event.getEnd().getDate().toString());
                        endDateTime = endDate.atStartOfDay().plusDays(1); // Add 1 day to include the whole end date
                    }

                    // Create entry only if startDateTime and endDateTime are set
                    if (startDateTime != null && endDateTime != null) {
                        Entry<String> entry = new Entry<>(summary);
                        entry.setInterval(startDateTime, endDateTime);
                        calendar.addEntry(entry);
                    }
                }
            }

            // Add the calendar to the calendar source
            calendarSource.getCalendars().add(calendar);

            // Set the calendar source to the calendar view
            calendarView.getCalendarSources().setAll(calendarSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void closeDialog(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

// Method to create custom entry dialog content with input fields
private Node createCustomEntryDialogContent(DateControl.EntryDetailsParameter param) {
    // Create a GridPane to layout the input fields
    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    // Add labels and input fields for title, start date, and end date
    Label titleLabel = new Label("Title:");
    TextField titleField = new TextField(param.getEntry().getTitle());
    Label startDateLabel = new Label("Start Date:");
    DatePicker startDatePicker = new DatePicker(param.getEntry().getStartDate());
    Label endDateLabel = new Label("End Date:");
    DatePicker endDatePicker = new DatePicker(param.getEntry().getEndDate());

    // Add labels and input fields to the gridPane
    gridPane.add(titleLabel, 0, 0);
    gridPane.add(titleField, 1, 0);
    gridPane.add(startDateLabel, 0, 1);
    gridPane.add(startDatePicker, 1, 1);
    gridPane.add(endDateLabel, 0, 2);
    gridPane.add(endDatePicker, 1, 2);

    // Create the OK button
    Button okButton = new Button("Apply");
    okButton.setOnAction(event -> {
        // Retrieve the values from the input fields
        String title = titleField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Validate the input
        if (title.isEmpty() || startDate == null || endDate == null) {
            showErrorAlert("Please fill in all fields.");
            return;
        }

        // Convert LocalDate to ZonedDateTime
        ZonedDateTime startDateTime = startDate.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endDateTime = endDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1); // Add 1 day to include the whole end date

        // Update or add event to Google Calendar
        updateOrCreateEventInGoogleCalendar(title, startDateTime, endDateTime);

        // Close the dialog
        ((Stage) okButton.getScene().getWindow()).close();
        // Update CalendarFX entry if it exists
        if (param.getEntry() != null) {
            // Retrieve the entry
            Entry<?> entry = param.getEntry();
            // Update entry properties
            entry.setTitle(title);
            entry.setInterval(startDateTime.toLocalDateTime(), endDateTime.toLocalDateTime().minusDays(1));
        }
    });

    // Add the OK button to the gridPane
    gridPane.add(okButton, 0, 3, 2, 1);
    return gridPane;
}
    private void updateOrCreateEventInGoogleCalendar(String title, ZonedDateTime startTime, ZonedDateTime endTime) {
        try {
            System.out.println("HERE 1");
            com.google.api.services.calendar.Calendar service = getCalendarService();

            // Check if the event already exists in Google Calendar
            String eventId = getEventIdFromGoogleCalendar(service, title, startTime, endTime);

            if (eventId != null) {
                // Event already exists, update it
                updateEventInGoogleCalendar(service, eventId, title, startTime, endTime);
            } else {
                // Event doesn't exist, create it
                createEventInGoogleCalendar(service, title, startTime, endTime);
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            showErrorAlert("Failed to update or create event in Google Calendar.");
        }
    }
    private String getEventIdFromGoogleCalendar(com.google.api.services.calendar.Calendar service, String title, ZonedDateTime startTime, ZonedDateTime endTime) throws IOException {
        // Implement logic to search for the event in Google Calendar and return its ID if found
        // Define the time range for the events query
        Date startDate = Date.from(startTime.toInstant());
        Date endDate = Date.from(endTime.toInstant());

        // Query the calendar events
        Events events = service.events().list("63f3800bafaea2d61e97715d8096a30c7ee0918b3b3238e61d6baabe02fa1b67@group.calendar.google.com") // 'primary' represents the primary calendar
                .setTimeMin(new com.google.api.client.util.DateTime(startDate))
                .setTimeMax(new com.google.api.client.util.DateTime(endDate))
                .setQ(title) // Search for events with the specified title
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();

        // Check if any events match the search criteria
        if (!items.isEmpty()) {
            // Iterate through the events to find a matching event
            for (Event event : items) {
                // Compare event title, start time, and end time
                if (event.getSummary().equals(title)) {
                    // Check if event start time matches
                    Date eventStartTime = event.getStart().getDateTime() != null ?
                            new Date(event.getStart().getDateTime().getValue()) :
                            new Date(event.getStart().getDate().getValue());

                    if (eventStartTime.equals(startDate)) {
                        // Check if event end time matches
                        Date eventEndTime = event.getEnd().getDateTime() != null ?
                                new Date(event.getEnd().getDateTime().getValue()) :
                                new Date(event.getEnd().getDate().getValue());

                        if (eventEndTime.equals(endDate)) {
                            // Return the event ID if found
                            return event.getId();
                        }
                    }
                }
            }
        }

        // Return null if no matching event is found
        return null;
    }

    private void updateEventInGoogleCalendar(com.google.api.services.calendar.Calendar service, String eventId, String title, ZonedDateTime startTime, ZonedDateTime endTime) throws IOException {
        // Implement logic to update the existing event in Google Calendar with new details
        // Retrieve the existing event from Google Calendar
        Event event = service.events().get("63f3800bafaea2d61e97715d8096a30c7ee0918b3b3238e61d6baabe02fa1b67@group.calendar.google.com", eventId).execute();

        // Update event details
        event.setSummary(title);

        // Set start time
        EventDateTime eventStart = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(Date.from(startTime.toInstant())))
                .setTimeZone(startTime.getZone().getId());
        event.setStart(eventStart);

        // Set end time
        EventDateTime eventEnd = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(Date.from(endTime.toInstant())))
                .setTimeZone(endTime.getZone().getId());
        event.setEnd(eventEnd);

        // Update the event in Google Calendar
        service.events().update("63f3800bafaea2d61e97715d8096a30c7ee0918b3b3238e61d6baabe02fa1b67@group.calendar.google.com", eventId, event).execute();
    }

    private void createEventInGoogleCalendar(com.google.api.services.calendar.Calendar service, String title, ZonedDateTime startTime, ZonedDateTime endTime) throws IOException {
        // Implement logic to create a new event in Google Calendar
        // Create a new event object
        Event event = new Event();

        // Set event details
        event.setSummary(title);

        // Set start time
        EventDateTime eventStart = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(Date.from(startTime.toInstant())))
                .setTimeZone(startTime.getZone().getId());
        event.setStart(eventStart);

        // Set end time
        EventDateTime eventEnd = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(Date.from(endTime.toInstant())))
                .setTimeZone(endTime.getZone().getId());
        event.setEnd(eventEnd);

        // Insert the event into Google Calendar
        service.events().insert("63f3800bafaea2d61e97715d8096a30c7ee0918b3b3238e61d6baabe02fa1b67@group.calendar.google.com", event).execute();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleCalendarAuth.class.getResourceAsStream(Auth.getCLIENT_SECRET_FILE());
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(Auth.getJSON_FACTORY(), new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, Auth.getJSON_FACTORY(), clientSecrets, Auth.getSCOPES())
                .setDataStoreFactory(new FileDataStoreFactory(new File(Auth.getCREDENTIALS_FOLDER())))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // Inside the startEventUpdateTimer method
    private void startEventUpdateTimer() {
        // Schedule a TimerTask to run every UPDATE_INTERVAL_MS milliseconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    fetchGoogleCalendarEventsAndUpdateListView();
                    // Update the calendar view on the JavaFX Application Thread
                    Platform.runLater(() -> updateCalendarView());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exceptions, for example, show an alert
                    Platform.runLater(() -> showErrorAlert(e.getMessage()));
                }
            }
        }, 0, 60000);
    }

    // Define a method to update the calendar view
    private void updateCalendarView() {
        try {
            // Fetch events from Google Calendar and update the calendar view
            initializeCalendarFX();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, for example, show an alert
            showErrorAlert(e.getMessage());
        }
    }

    // Define a method to show error alerts
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to add an event to Google Calendar
    private void addEventToGoogleCalendar(String summary, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            com.google.api.services.calendar.Calendar service = getCalendarService();

            Event event = new Event()
                    .setSummary(summary)
                    .setStart(new EventDateTime().setDateTime(new DateTime(startDateTime.toString())))
                    .setEnd(new EventDateTime().setDateTime(new DateTime(endDateTime.toString())));

            String calendarId = "63f3800bafaea2d61e97715d8096a30c7ee0918b3b3238e61d6baabe02fa1b67@group.calendar.google.com";
            service.events().insert(calendarId, event).execute();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            showErrorAlert("Failed to add event to Google Calendar.");
        }
    }

    // Method to get Google Calendar service
    private com.google.api.services.calendar.Calendar getCalendarService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, Auth.getJSON_FACTORY(), getCredentials(HTTP_TRANSPORT))
                .setApplicationName(Auth.getAPPLICATION_NAME())
                .build();
    }
}
