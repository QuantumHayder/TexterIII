package com.example.client.Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField usermodeField;


    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String usermode = usermodeField.getText().trim();
        //Check all fields are not empty
        if (username.isEmpty() || usermode.isEmpty()) {
            showAlert("Empty Fields Validation Error", "Missing Information", "Both username and usermode are required.");
        } else if (!(usermode.equalsIgnoreCase("Editor")  || usermode.equalsIgnoreCase("Viewer"))) {
            showAlert("UserMode Validation Error", "Wrong Information", "Usermode must be either 'Editor' or 'Viewer'");
        }else {
            try {
                int userId = sendCreateClientRequest(username, usermode);
                navigateToDocumentSelection(username, usermode, userId);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                showAlert("Server Error", "Client Creation Failed", "Could not create client.");
            }
        }
    }

    private int sendCreateClientRequest(String username, String usermode) throws IOException, InterruptedException {
        var client = java.net.http.HttpClient.newHttpClient();
    
        String requestBody = String.format("{\"username\":\"%s\", \"usermode\":\"%s\"}",
                                           username, usermode.toUpperCase());
    
        var request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8080/client"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    
        var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
    
        if (response.statusCode() != 200) {
            throw new IOException("Failed to create client: " + response.body());
        }
    
        // Parse JSON and extract userUID
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());
    
        return json.get("userUID").asInt();
    }
    
    
    
    private void navigateToDocumentSelection(String username, String usermode, int userId) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/document_selection.fxml")); 
    
        Scene scene = new Scene(loader.load());
    
        DocumentSelectionController controller = loader.getController();
        controller.initializeData(userId, username, usermode);
    
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Select or Join Document");
        stage.show();
    }
    
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
