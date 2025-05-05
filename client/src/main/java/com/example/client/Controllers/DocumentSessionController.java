package com.example.client.Controllers;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.data.util.Pair;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.example.client.DTO.CollaboratorDTO;
import com.example.client.Models.ClientModel.UserMode;
import com.example.client.Services.DocumentService;
import com.example.client.Utils.Helper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DocumentSessionController {
    @FXML private TextArea editorTextArea;
    @FXML private TextField editorCodeField;
    @FXML private TextField viewerCodeField;
    @FXML private ListView<CollaboratorDTO> collaboratorsList;

    private final DocumentService documentService = new DocumentService();
    private final Helper helper = new Helper();

    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    private UUID documentId;
    private String viewCode;
    private String editCode;
    private List<CollaboratorDTO> collaborators;
    private int ownerId;
    private String rootNodeId;
    private String docText;

    public void initializeData(UUID documentId, String viewCode, String editCode, List<CollaboratorDTO> collaborators,
                               int ownerId, String rootNodeId, String docText) {
        this.documentId = documentId;
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

        connectToWebSocketAndSubscribe();
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

    private void updateCollaborators(List<CollaboratorDTO> collaborators) {
        Platform.runLater(() -> {
            collaboratorsList.getItems().clear();
            collaboratorsList.getItems().addAll(collaborators);
            
        });
    }

    private void connectToWebSocketAndSubscribe() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        try {
            System.out.println("Attempting WebSocket connection...");
            stompSession = stompClient.connect("ws://localhost:8080/texter", new MyStompSessionHandler()).get();
            System.out.println("WebSocket connection successful.");

            // Subscribe to collaborator updates
            String topic = "/topic/document/" + documentId + "/collaborators";
            stompSession.subscribe(topic, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return CollaboratorDTO[].class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    CollaboratorDTO[] collaborators = (CollaboratorDTO[]) payload;
                    updateCollaborators(List.of(collaborators));
                    System.out.println("📥 Received collaborators update: " + List.of(collaborators));
                }
            });

            System.out.println("Connected and subscribed to collaborators.");

        } catch (InterruptedException | ExecutionException e) {
            helper.showAlert("WebSocket Error","Document Session Controller: connectToWebsocketAndSubscribe" ,"Could not connect to server: " + e.getMessage());
        }
    }

    class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("✅ Connected to WebSocket STOMP broker.");
        }
    }
}
