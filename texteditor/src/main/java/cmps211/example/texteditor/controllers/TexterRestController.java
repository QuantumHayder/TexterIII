package cmps211.example.texteditor.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cmps211.example.texteditor.DTO.ClientRequest;
import cmps211.example.texteditor.DTO.ClientResponseDTO;
import cmps211.example.texteditor.DTO.DocumentRequestDTO;
import cmps211.example.texteditor.DTO.DocumentResponseDTO;
import cmps211.example.texteditor.service.Implementations.ClientService;
import cmps211.example.texteditor.service.Implementations.DocumentService;


@RestController   //HTTP
public class TexterRestController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ClientService clientService;

    
    @PostMapping("/client")
    public ClientResponseDTO createClient(@RequestBody ClientRequest request) {
        return clientService.createClient(request.getUsername(), request.getUsermode());
    }
    @PostMapping("/create")
    public ResponseEntity<DocumentResponseDTO> createDocument(@RequestBody DocumentRequestDTO request) {
        System.out.println("Received clientUID: " + request.getClientUID()); // debug
        try {
            DocumentResponseDTO response = documentService.createDocument(request.getClientUID());
            return ResponseEntity.ok(response); 
        } catch (IllegalArgumentException e) {
            System.err.println("Error in createDocument: " + e.getMessage()); // Log the error
            return ResponseEntity.badRequest().body(new DocumentResponseDTO(null, null, null, null, null,null)); // Return empty response with 400
        }
    }
    @PostMapping("/register/{documentId}")
    public ResponseEntity<DocumentResponseDTO> registerToDocument(
        @PathVariable String documentId,
        @RequestBody DocumentRequestDTO request) {

    try {
        DocumentResponseDTO response = documentService.registerToDocument(documentId, request.getClientUID());
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        System.err.println("Error in registerToDocument: " + e.getMessage()); // Log the error
        return ResponseEntity.badRequest().body(new DocumentResponseDTO(null, null, null, null, null,null)); // Return empty response with 400
    }
}
    /*@PostMapping("/upload")
    public String uploadDocument(DocumentDTO request) {
        return documentService.uploadDocument(request.getDocumentId(), request.getTextContent());
    }

    @GetMapping("/download/{documentId}") 
    public DocumentDTO downloadDocument(@PathVariable UUID documentId){
        return documentService.downloadDocument(documentId);
    }*/


}
