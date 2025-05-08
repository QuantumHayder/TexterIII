package cmps211.example.texteditor.service.Implementations;


import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cmps211.example.texteditor.crdt.CRDTAlgorithm;
import cmps211.example.texteditor.crdt.Node;
import cmps211.example.texteditor.models.ClientModel;
import cmps211.example.texteditor.models.DocumentModel;
import cmps211.example.texteditor.repository.ClientRepository;
import cmps211.example.texteditor.repository.DocumentRepository;
import cmps211.example.texteditor.repository.NodeRepository;
import cmps211.example.texteditor.service.Interfaces.ITextFileImporter;

@Service
public class TextFileImporter implements ITextFileImporter {
    @Autowired
    private DocumentRepository docRepo;

    @Autowired
    private NodeRepository nodeRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private CRDTAlgorithm crdtAlgorithm;

    @Override
    public UUID importTextFile(MultipartFile file, String username) throws Exception {
        String content = new String(file.getBytes());
        if (content.isEmpty()) throw new IllegalArgumentException("Uploaded file is empty");

        // Create root node
        Node root = new Node(-1, '\0', null);
        nodeRepo.save(root); // Persist root to get its ID

        // Create document
        DocumentModel doc = new DocumentModel();
        doc.setRoot(root);
        doc.setOwnerId(0); // You may get this from client object
        docRepo.save(doc);

        // Create client and associate it
        ClientModel client = new ClientModel();
        client.setUsername(username);
        client.setCode(""); // set if needed
        client.setMode(ClientModel.UserMode.EDITOR);
        client.setDocument(doc);
        clientRepo.save(client);

        // Insert character nodes
        Node previous = root;
        for (char c : content.toCharArray()) {
            Node newNode = new Node(1, Instant.now(), c, null); // Adjust site ID if needed
            crdtAlgorithm.insertNode(previous, newNode);
            nodeRepo.save(newNode);
            previous = newNode;

            Thread.sleep(1); // Ensure different timestamps (or use ChronoUnit)
        }

        return doc.getUID();
    }
}
