package com.example.client.Controllers;

import com.example.client.DTO.DocumentResponseDTO;
import com.example.client.DTO.DocumentSessionDTO;
import com.example.client.Models.ClientModel.UserMode;
import com.example.client.Services.DocumentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.util.Pair;

public class DocumentSelectionController {
    private int userId;
    private String username;
    private String usermode;

    private final DocumentService documentService = new DocumentService();

    @FXML
    private TextField joinCodeField;

    public void initializeData(int userId, String username, String usermode) {
        this.userId = userId;
        this.username = username;
        this.usermode = usermode;
    }

    @FXML
    private void handleCreateNewDocument() {
        try {
            DocumentResponseDTO session = documentService.createDocument(userId);
            navigateToJoinedDocument(session.getDocUID(), session.getViewCode(), session.getEditCode(),
                    session.getCollaborators(), session.getOwnerId(), session.getRootNodeId(), session.getDocText());
        } catch (Exception e) {
            showAlert("Error", "Document Creation Failed", e.getMessage());
        }
    }

    @FXML
    private void handleJoinDocument() {
        String docCode = joinCodeField.getText().trim();
        if (docCode.isEmpty()) {
            showAlert("Join Error", "Empty Field", "Document code cannot be empty.");
            return;
        }

        try {
            DocumentResponseDTO session = documentService.registerToDocument(docCode, userId);
            navigateToJoinedDocument(session.getDocUID(), session.getViewCode(), session.getEditCode(),
                    session.getCollaborators(), session.getOwnerId(), session.getRootNodeId(), session.getDocText());
        } catch (Exception e) {
            showAlert("Error", "Join Failed", e.getMessage());
        }
    }

    private void navigateToJoinedDocument(UUID documentId, String editorCode, String viewerCode,
                                          List<Pair<String, UserMode>> collaborators, int ownerId, String rootNodeId, String content) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/document_session.fxml"));
        Scene scene = new Scene(loader.load());

        DocumentSessionController controller = loader.getController();
        controller.initializeData(documentId,viewerCode, editorCode, collaborators, ownerId, rootNodeId, content);
        controller.setInitialText(content);

        Stage stage = (Stage) joinCodeField.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Document: " + documentId);
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
