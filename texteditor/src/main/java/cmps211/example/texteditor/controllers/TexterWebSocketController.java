package cmps211.example.texteditor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import cmps211.example.texteditor.DTO.ClientResponseDTO;
import cmps211.example.texteditor.models.Message;
import cmps211.example.texteditor.service.Implementations.DocumentService;

@Controller
public class TexterWebSocketController {
    @Autowired
    private DocumentService DocumentService;

    @MessageMapping("/type")
    @SendTo("/topic/document")
    public Message typeCharacter(@Payload String[] payload) {
        int userId = Integer.parseInt(payload[0]);
        String documentId = payload[1];
        char character =payload[2].charAt(0);
        String parentId = payload[3];

        String resultMessage = DocumentService.typeCharacter(userId,documentId,character,parentId);
        boolean isError = resultMessage.startsWith("Invalid");
        //1 = Node not created, 0 = Node created
        return new Message(character,isError);
    }

    @MessageMapping("/delete")
    @SendTo("/topic/document")
    public Message removeCharacter(@Payload String[] payload) {
        char character = payload[0].charAt(0);
        String nodeId = payload[1];
        String resultMessage = DocumentService.deleteCharacter(nodeId);
        boolean isError = resultMessage.startsWith("Invalid");
        //1 = Node not created, 0 = Node created
        return new Message(character,isError);
    }

    /* 
    @MessageMapping("/undo")
    @SendTo("topic/document")
    public Message undo() {
        return Message();
    }

    @MessageMapping("/redo")
    @SendTo("topic/document")
    public Message redo() {
        return Message();
    }
    */
}
