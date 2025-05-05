package cmps211.example.texteditor.controllers;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cmps211.example.texteditor.DTO.ClientRequest;
import cmps211.example.texteditor.DTO.DocumentRequestDTO;
import cmps211.example.texteditor.DTO.DocumentResponseDTO;
import cmps211.example.texteditor.service.Implementations.ClientService;
import cmps211.example.texteditor.service.Implementations.DocumentService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.UUID;

@RestController   //HTTP
public class TexterRestController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ClientService clientService;

    
    @PostMapping("/client")
    public int createClient(@RequestBody ClientRequest request) {
        return clientService.createClient(request.getUsername(), request.getUsermode());
    }
    @PostMapping("/create")
    public ResponseEntity<DocumentResponseDTO> createDocument(@RequestBody DocumentRequestDTO request) {
        try {
            DocumentResponseDTO response = documentService.createDocument(request.getClientUID());
            return ResponseEntity.ok(response); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
        return ResponseEntity.badRequest().build(); // Optionally return error message
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
    
    // SH added + please check that there are no missing endpoints
    // functions not  implemented needs to be implemented
    @GetMapping("/api/document/download/{documentId}")
    public ResponseEntity<String> downloadDocument(@PathVariable UUID documentId) {
    try {
        String content = documentService.downloadDocument(documentId).getTextContent();
        return ResponseEntity.ok(content);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestBody DocumentRequestDTO request) {
        return documentService.uploadDocument(request.getDocumentId(), request.getTextContent());
    }


}
