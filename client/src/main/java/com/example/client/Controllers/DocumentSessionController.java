package com.example.client.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.util.Pair;

import com.example.client.DTO.CollaboratorDTO;
import com.example.client.Models.ClientModel.UserMode;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DocumentSessionController {
    @FXML private TextArea editorTextArea;
    @FXML private TextField editorCodeField;
    @FXML private TextField viewerCodeField;
    @FXML private ListView<CollaboratorDTO> collaboratorsList;

    private UUID userId;
    private String viewCode;
    private String editCode;
    private List<CollaboratorDTO> collaborators;
    private int ownerId;
    private String rootNodeId;
    private String docText;

    public void initializeData(UUID userId, String viewCode, String editCode, List<CollaboratorDTO> collaborators,
                               int ownerId, String rootNodeId, String docText) {
        this.userId = userId;
        this.viewCode = viewCode;
        this.editCode = editCode;
        this.collaborators = collaborators;
        this.ownerId = ownerId;
        this.rootNodeId = rootNodeId;
        this.docText = docText;

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

}
