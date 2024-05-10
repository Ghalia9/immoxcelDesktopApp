package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AiAssistant {
    @FXML
    private WebView webView;
    public void loadChatbot() {
        WebEngine webEngine = webView.getEngine();
        String chatbotURL = "https://www.chatbase.co/chatbot-iframe/S7e8bEbx9v9PxK0c5Pef6";
        webEngine.load(chatbotURL);
    }
}
