package cmps211.example.texteditor.controllers;

import cmps211.example.texteditor.DTO.CharacterResponseDTO;
import cmps211.example.texteditor.DTO.CharacterTypePayload;
import cmps211.example.texteditor.service.Implementations.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.util.UUID;

@Controller
public class TexterWebSocketController {
    @Autowired
    private DocumentService documentService;

    @MessageMapping("/character/type/{docId}")
    public void typeCharacter(@Payload CharacterTypePayload payload, @org.springframework.messaging.handler.annotation.DestinationVariable UUID docId) {
        System.out.println("🔵 WebSocket message received:");
        System.out.println("  ↳ UserID: " + payload.getUserId());
        System.out.println("  ↳ Char: " + payload.getCharacter());
        System.out.println("  ↳ ParentID: " + payload.getParentNodeId());
        System.out.println("  ↳ DocID: " + docId);

        CharacterResponseDTO response = documentService.handleTypedCharacter(
            payload.getUserId(),
            docId,
            payload.getCharacter(),
            payload.getParentNodeId()
        );

        System.out.println("✅ Node created. Sending to topic /topic/document/" + docId + "/children");
        documentService.getMessagingTemplate().convertAndSend(
            "/topic/document/" + docId + "/children",
            response
        );
    }

    @MessageMapping("/character/delete/{docId}")
    public void deleteCharacter(@Payload String nodeId, @org.springframework.messaging.handler.annotation.DestinationVariable String docId) {
        documentService.deleteCharacter(nodeId); // Optionally: broadcast deletion as well
    }
}
