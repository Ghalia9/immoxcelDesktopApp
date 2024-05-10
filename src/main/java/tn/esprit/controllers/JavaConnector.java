package tn.esprit.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class JavaConnector {
    public LoginController loginController;
    public void setLoginController(LoginController login)
    {
        this.loginController=login;
    }
    public void onRecaptchaSuccess(boolean isSuccess) {
        // Store the result in the variable

        // Call the login function with the verification result
        loginController.setCaptcha(isSuccess);
    }
}

