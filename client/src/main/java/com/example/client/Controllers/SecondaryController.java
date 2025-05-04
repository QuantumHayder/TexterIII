package com.example.client.Controllers;

import java.io.IOException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SecondaryController {
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    private StompSession stompSession;
    private String username;
    private String roomId;

   /*  public void initializeData( String username, String roomId) {
        this.username = username;
        this.roomId = roomId;
        connectToWebSocket(username, roomId);
        // Subscribe to the chat room
        String topic = "/topic/chat/" + roomId;
        stompSession.subscribe(topic, new StompFrameHandler() {
                 @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ChatMessage.class; // Expected payload type
                }
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    ChatMessage message = (ChatMessage) payload;
                    Platform.runLater(() -> chatArea.appendText("[" + message.getUsername() + "]: " + message.getContent() + "\n"));
                }
        });
    }*/
    private void connectToWebSocket(String username, String roomId) {
        try {
            // Create WebSocket client
            WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
            // Use json message converter for chat message objects
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    
            // Connect to the WebSocket server
            String url = "ws://localhost:8080/ws";
            this.stompSession=stompClient.connect(url, new MyStompSessionHandler()).get();
            
    
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Failed to Connect to WebSocket");
            alert.setContentText("An error occurred while connecting to the WebSocket server.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            // Send the message to the WebSocket server
            String destination = "/app/chat/" + roomId;
            /*ChatMessage message = new ChatMessage();
            message.setUsername(username);
            message.setContent(content);
            stompSession.send(destination, message);
            messageField.clear();*/
        }
    }
}
class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected to WebSocket server!");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("An error occurred: " + exception.getMessage());
    }
}
