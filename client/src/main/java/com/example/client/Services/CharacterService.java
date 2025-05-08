package com.example.client.Services;

import com.example.client.DTO.NodeDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.example.client.DTO.CharacterResponseDTO;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

@Setter
@Getter
public class CharacterService {

    private final StompSession stompSession;

    public CharacterService(StompSession stompSession) {
        this.stompSession = stompSession;
    }

    public void typeCharacter(UUID documentId, int userId, char character, String parentNodeId) {
        String destination = "/app/character/type/" + documentId;
        // Send a small object or simple map (Spring can handle it as JSON)
        stompSession.send(destination, new SimpleCharacterInput(userId, character, parentNodeId));
        System.out.println("Typed character '" + character + "' sent by user=" + userId+ " with parent=" + parentNodeId);
    }

    public void deleteCharacter(String documentId, String nodeIdToDelete) {
        String destination = "/app/character/delete/" + documentId;
        stompSession.send(destination, nodeIdToDelete);
        System.out.println("Deletion request sent for node: " + nodeIdToDelete);
    }

    public void subscribeToCharacterUpdates(String documentId, BiConsumer<String, List<NodeDTO>> handler) {
        String topic = "/topic/document/" + documentId + "/children";
        stompSession.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CharacterResponseDTO.class;
            }
    
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                CharacterResponseDTO response = (CharacterResponseDTO) payload;
                String parentId = response.getParentId();
                List<NodeDTO> children = response.getChildren();
    
                handler.accept(parentId, children);  // ← this is how we pass both items to the client logic
                System.out.println("Received updated children for parent: " + parentId);
            }
        });
    }    

    // Inner class for sending typing input
    private record SimpleCharacterInput(int userId, char character, String parentNodeId) {}
}
