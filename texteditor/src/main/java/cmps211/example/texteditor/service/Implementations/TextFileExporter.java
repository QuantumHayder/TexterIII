package cmps211.example.texteditor.service.Implementations;

import cmps211.example.texteditor.crdt.CRDTAlgorithm;
import cmps211.example.texteditor.crdt.Node;
import cmps211.example.texteditor.models.DocumentModel;
import cmps211.example.texteditor.repository.DocumentRepository;
import cmps211.example.texteditor.service.Interfaces.ITextFileExporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TextFileExporter implements ITextFileExporter {

    private static final String EXPORT_DIRECTORY = "C:\\Users\\Abd El-Rahman Hayder\\Downloads";
    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private CRDTAlgorithm crdtAlgo;
	
	private Logger logger = LoggerFactory.getLogger(TextFileExporter.class);
	
	@Override
	public Path export(UUID documentId) {
        Optional<DocumentModel> docOpt = docRepo.findById(documentId);
        if (docOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid document ID: " + documentId); // cleaner than returning string
        }
    
        DocumentModel document = docOpt.get();
        String filename = "TexterDocument.txt";
        Node rootNode = document.getRoot();
        String filecontent = crdtAlgo.getDocumentText(rootNode);
		Path filePath = Paths.get(EXPORT_DIRECTORY, filename);
		try {
			Path exportedFilePath = Files.write(filePath, filecontent.getBytes(), StandardOpenOption.CREATE);
			return exportedFilePath;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}	
		throw new IllegalStateException("Failed to export document to file");
	}
}
