package cmps211.example.texteditor.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cmps211.example.texteditor.DTO.ClientRequest;
import cmps211.example.texteditor.DTO.ClientResponseDTO;
import cmps211.example.texteditor.DTO.DocumentRequestDTO;
import cmps211.example.texteditor.DTO.DocumentResponseDTO;
import cmps211.example.texteditor.service.Implementations.ClientService;
import cmps211.example.texteditor.service.Implementations.DocumentService;
import cmps211.example.texteditor.service.Implementations.TextFileExporter;
import cmps211.example.texteditor.service.Implementations.TextFileImporter;

@RestController
public class TexterRestController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ClientService clientService;
    @Autowired
	private TextFileExporter fileExporter;
   
    private final TextFileImporter fileImporter;
    @Autowired
    public TexterRestController(TextFileImporter fileImporter) {
        this.fileImporter = fileImporter;
    }

    
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
    @PostMapping("/upload") 
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("username") String username) {
        try {
            UUID documentId = fileImporter.importTextFile(file, username);
            return ResponseEntity.ok("File uploaded and document created with ID: " + documentId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @RequestMapping("/download/{documentId}") 
    public ResponseEntity<InputStreamResource> downloadTextFileExample1(@PathVariable UUID documentId) throws FileNotFoundException {
	
	Path exportedPath = fileExporter.export(documentId);
		
	File exportedFile = exportedPath.toFile();
	FileInputStream fileInputStream = new FileInputStream(exportedFile);
	InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
	
	return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + exportedPath.getFileName())
			.contentType(MediaType.TEXT_PLAIN)
			.contentLength(exportedFile.length())
			.body(inputStreamResource);
}


}
