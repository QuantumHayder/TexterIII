package com.example.client.Controllers;

import com.example.client.DTO.CollaboratorDTO;
import com.example.client.DTO.DocumentResponseDTO;
import com.example.client.DTO.DocumentSessionDTO;
import com.example.client.Models.ClientModel.UserMode;
import com.example.client.Services.DocumentService;
import com.example.client.Utils.Helper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


public class DocumentSelectionController {
    private int userId;
    //private String username;
    //private String usermode;
    private final Helper helper = new Helper();


    private final DocumentService documentService = new DocumentService();

    @FXML
    private TextField joinCodeField;

    public void initializeData(int userId, String username, String usermode) {
        this.userId = userId;
       // this.username = username;
        //this.usermode = usermode;
    }

    @FXML
    private void handleCreateNewDocument() {
        try {
            DocumentResponseDTO session = documentService.createDocument(userId);

            System.out.println("Received DTO in controller:");
            System.out.println("  docId: " + session.getDocumentId());
            System.out.println("  collaborators: " + session.getCollaborators());
            System.out.println("  rootNodeId: " + session.getRootNodeId());

            navigateToJoinedDocument(session.getDocumentId(), session.getViewCode(), session.getEditCode(),
                    session.getCollaborators(), session.getOwnerId(), userId,session.getRootNodeId(), session.getTextContent());
        } catch (Exception e) {
            System.err.println("Exception occurred in handleCreateNewDocument()");
            e.printStackTrace();  
            helper.showErrorDialog("Document Creation Failed", e.getClass().getSimpleName() + ": " + e.getMessage());

        }
    }

    @FXML
    private void handleJoinDocument() {
        String docCode = joinCodeField.getText().trim();
        if (docCode.isEmpty()) {
            helper.showAlert("Join Error", "Empty Field", "Document code cannot be empty.");
            return;
        }

        try {
            DocumentResponseDTO session = documentService.registerToDocument(docCode, userId);
            navigateToJoinedDocument(session.getDocumentId(), session.getViewCode(), session.getEditCode(),
                    session.getCollaborators(), session.getOwnerId(), this.userId, session.getRootNodeId(), session.getTextContent());
        } catch (Exception e) {
            helper.showAlert("Error", "Join Failed", e.getMessage());
        }
    }

    private void navigateToJoinedDocument(UUID documentId, String editorCode, String viewerCode,
                                          List<CollaboratorDTO> collaborators, int ownerId,int userId ,String rootNodeId, String content) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/document_session.fxml"));
        Scene scene = new Scene(loader.load());

        DocumentSessionController controller = loader.getController();
        controller.initializeData(documentId,viewerCode, editorCode, collaborators, userId, ownerId ,rootNodeId, content);
        controller.setInitialText(content);

        Stage stage = (Stage) joinCodeField.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Document: " + documentId);
        stage.show();
    }

    
}
