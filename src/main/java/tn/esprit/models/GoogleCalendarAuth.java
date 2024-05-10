package tn.esprit.models;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

import java.util.Collections;
import java.util.List;

public class GoogleCalendarAuth {
    private  final String APPLICATION_NAME = "MVC Google Calendar";
    private  final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private  final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private  final String CLIENT_SECRET_FILE = "/client_secret_548803311291-kq6336v8s3ki3gk0v93a0vn6m5jjbpad.apps.googleusercontent.com.json";
    private  final String CREDENTIALS_FOLDER = "/GoogleCalendarCredentials";

    public String getAPPLICATION_NAME() {
        return APPLICATION_NAME;
    }

    public JsonFactory getJSON_FACTORY() {
        return JSON_FACTORY;
    }

    public List<String> getSCOPES() {
        return SCOPES;
    }

    public String getCLIENT_SECRET_FILE() {
        return CLIENT_SECRET_FILE;
    }

    public String getCREDENTIALS_FOLDER() {
        return CREDENTIALS_FOLDER;
    }
}
