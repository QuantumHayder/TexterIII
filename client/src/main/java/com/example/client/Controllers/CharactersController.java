package com.example.client.Controllers;

import java.util.Collections;
import java.util.UUID;

import org.springframework.messaging.simp.stomp.StompSession;

import com.example.client.DTO.CharacterResponseDTO;
import com.example.client.DTO.NodeDTO;
import com.example.client.Models.CrdtBuffer;
import com.example.client.Services.CharacterService;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class CharactersController {
    private final CharacterService characterService;
    private final CrdtBuffer crdtBuffer;
    private final TextArea editorTextArea;
    private final int userId;  // ← store this


    public CharactersController(StompSession stompSession, TextArea editorTextArea, int userId) {
        this.characterService = new CharacterService(stompSession);
        this.crdtBuffer = new CrdtBuffer();
        this.editorTextArea = editorTextArea;
        this.userId = userId;
    }

    public void initializeSubscription(UUID documentId) {
        characterService.subscribeToCharacterUpdates(
          documentId.toString(),
          (parentId, children) -> {
            crdtBuffer.insertChildren(parentId, children);
            Platform.runLater(() -> {
              String full = crdtBuffer.toString();
              int oldCaret = editorTextArea.getCaretPosition();
              editorTextArea.setText(full);
              int pos = Math.min(oldCaret, full.length());
              editorTextArea.positionCaret(pos);
            });
          }
        );
    }
    

    public void sendCharacterTyped(UUID documentId, char character, String parentNodeId) {
        System.out.println("[CLIENT] sendCharacterTyped( doc=" + documentId +
                       ", char='" + character +
                       "', parent=" + parentNodeId + " )");
        characterService.typeCharacter(documentId, userId,character, parentNodeId);
    }

    public void sendCharacterDeleted(UUID documentId, String nodeIdToDelete) {
        characterService.deleteCharacter(documentId.toString(), nodeIdToDelete);
    }

    public void addRootNode(NodeDTO rootNode) {
        crdtBuffer.addRoot(rootNode);
    }

    public boolean nodeExists(String nodeId) {
        return crdtBuffer.containsNode(nodeId);
    }
}
