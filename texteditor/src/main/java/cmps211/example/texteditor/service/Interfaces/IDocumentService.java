package cmps211.example.texteditor.service.Interfaces;

import java.util.UUID;

import cmps211.example.texteditor.DTO.DocumentResponseDTO;

public interface IDocumentService {
    
    public DocumentResponseDTO createDocument(int ownerId);
    public DocumentResponseDTO registerToDocument(String docId, int userId);
    public String typeCharacter(int userId, String docId, char value, String parentId);
    public String deleteCharacter (String nodeId);
    String uploadDocument(UUID documentId, String text);
    DocumentResponseDTO downloadDocument(UUID documentId);
}
