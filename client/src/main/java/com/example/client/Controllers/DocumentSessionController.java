package com.example.client.Controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.util.Pair;

import com.example.client.Models.ClientModel.UserMode;
import com.example.client.Services.DocumentService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

// the following are imports tht might be needed for future  
/*import javafx.stage.FileChooser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;*/

public class DocumentSessionController {
    private UUID currentDocumentId; // (SH added) This stores the document being edited

    @FXML private TextArea editorTextArea;
    @FXML private TextField editorCodeField;
    @FXML private TextField viewerCodeField;
    @FXML private ListView<Pair<String,UserMode>> collaboratorsList;

    private UUID userId;
    private String viewCode;
    private String editCode;
    private List<Pair<String, UserMode>> collaborators;
    private int ownerId;
    private String rootNodeId;
    private String docText;
    private final DocumentService documentService = new DocumentService(); // (SH added) msh 3aref eh lazmetha
    public void initializeData(UUID userId, String viewCode, String editCode, List<Pair<String, UserMode>> collaborators,
                               int ownerId, String rootNodeId, String docText) {
        this.userId = userId;
        this.viewCode = viewCode;
        this.editCode = editCode;
        this.collaborators = collaborators;
        this.ownerId = ownerId;
        this.rootNodeId = rootNodeId;
        this.docText = docText;
        this.currentDocumentId = response.getDocumentId();
        editorCodeField.setText(editCode);
        viewerCodeField.setText(viewCode);
        editorTextArea.setText(docText);

        collaboratorsList.getItems().addAll(collaborators);

        /* 
        if (usermode.toString().equalsIgnoreCase("VIEWER")) {
            editorTextArea.setEditable(false);
        }
            */
    }

    public void setInitialText(String text) {
        editorTextArea.setText(text);
    }    
    // Future: handle WebSocket connection, real-time updates, undo/redo, etc.
    
    // Functions for download
    @FXML
    private void handleDownload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(downloadButton.getScene().getWindow());

        if (file != null) {
             try {
                String content = DocumentService.downloadDocument(docId); // call the service
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                showAlert("Error saving file: " + e.getMessage());
            }
        }
    }

private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(message);
    alert.showAndWait();
}

@FXML
private void onDownloadClicked() {
    handleDownload(); 
}

// Functions for upload
@FXML
public void handleUploadDocument() {
    try {
        String text = editorTextArea.getText();
        String response = documentService.uploadDocument(currentDocumentId, text);
        showAlert("Upload Success", response);
    } catch (Exception e) {
        showAlert("Upload Failed", e.getMessage());
    }
}

private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

@FXML
private void onUploadClicked() {
    handleUploadDocument(); 
}


}
