package cmps211.example.texteditor.service.Implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cmps211.example.texteditor.DTO.CollaboratorDTO;
import cmps211.example.texteditor.DTO.DocumentResponseDTO;
import cmps211.example.texteditor.crdt.CRDTAlgorithm;
import cmps211.example.texteditor.crdt.Node;
import cmps211.example.texteditor.models.ClientModel;
import cmps211.example.texteditor.models.ClientModel.UserMode;
import cmps211.example.texteditor.models.DocumentModel;
import cmps211.example.texteditor.repository.ClientRepository;
import cmps211.example.texteditor.repository.DocumentRepository;
import cmps211.example.texteditor.repository.NodeRepository;
import cmps211.example.texteditor.service.Interfaces.IDocumentService;
import jakarta.annotation.PostConstruct;


@Service
public class DocumentService implements IDocumentService {
    
    //using autowired eliminates the need to add a constructor to initialize the private members
    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private NodeRepository nodeRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private CRDTAlgorithm crdtAlgo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public DocumentResponseDTO createDocument(int ownerId) {

        Optional<ClientModel> clientOpt = clientRepo.findById(ownerId);
        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid client ID: " + ownerId); // cleaner than returning string
        }
    
        ClientModel client = clientOpt.get();
        
        DocumentModel newDoc = new DocumentModel(ownerId);

        Node rootNode = new Node(ownerId, '\0', null);
        rootNode.markDeleted();
        rootNode.setDocument(newDoc); // set back-reference
        newDoc.setRoot(rootNode);
        newDoc.addNode(rootNode); // adds to node list

        // Set up client
        client.setDocument(newDoc);
        newDoc.addClient(client);

        // Save Document FIRST to get UID and cascade persist root node + client
        DocumentModel savedDoc = docRepo.save(newDoc);

        // Now set codes based on generated UID
        savedDoc.setEditCode("Edit-" + savedDoc.getUID());
        savedDoc.setViewCode("View-" + savedDoc.getUID());
        savedDoc = docRepo.save(savedDoc); // persist edit/view codes

        // Return response
        DocumentResponseDTO response = new DocumentResponseDTO();
        response.setDocumentId(savedDoc.getUID());
        response.setEditCode(savedDoc.getEditCode());
        response.setViewCode(savedDoc.getViewCode());
        response.setRootNodeId(savedDoc.getRoot().getUID());
        response.setTextContent("");
        
        String username = client.getUsername();
        UserMode mode = client.getMode();

        if (username == null || mode == null) {
            throw new IllegalArgumentException("Username or mode must not be null");
        }

        List<CollaboratorDTO> collaborators = new ArrayList<>();
        CollaboratorDTO member = new CollaboratorDTO(client.getUsername(), client.getMode());
        collaborators.add(member);
        response.setCollaborators(collaborators);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
}

        return response;
    }

    @Override
    public DocumentResponseDTO registerToDocument(String docId, int userId) {
        Optional<DocumentModel> docOpt = docRepo.findById(UUID.fromString(docId));
        Optional<ClientModel> clientOpt = clientRepo.findById(userId);

        if (docOpt.isEmpty() || clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid document or user ID.");
        }

        DocumentModel doc = docOpt.get();
        ClientModel client = clientOpt.get();

        String username = client.getUsername();
        UserMode mode = client.getMode();

        if (username == null || mode == null) {
            throw new IllegalArgumentException("Username or mode must not be null");
        }

        client.setDocument(doc);  
        doc.addClient(client);
        docRepo.save(doc);

        // Notify other users
        List<CollaboratorDTO> collaborators = doc.getClients().stream()
        .map(c -> new CollaboratorDTO(c.getUsername(), c.getMode()))
        .toList();

        messagingTemplate.convertAndSend(
            "/topic/document/" + doc.getUID() + "/collaborators",
            collaborators
        );

        DocumentResponseDTO response = new DocumentResponseDTO();
        response.setDocumentId(doc.getUID());
        response.setEditCode(doc.getEditCode());
        response.setViewCode(doc.getViewCode());
        response.setCollaborators(collaborators);
        response.setTextContent("");   //need to add the crdt method of traversing the tree
        response.setRootNodeId(doc.getRoot().getUID());  // You forgot this
        return response;
    }

    @Override
    public String typeCharacter(int userId, String docId, char value, String parentId) {
        Optional<Node> parentOpt = nodeRepo.findById(parentId);
        if (parentOpt.isEmpty()) {
            return "Invalid parent ID: " + parentId;
        }
    
        Node parent = parentOpt.get();

        Optional<DocumentModel> docOpt = docRepo.findById(UUID.fromString(docId));
        if(docOpt.isEmpty()) {
            return "Invalid document ID: " + docId;
        }
        DocumentModel doc = docOpt.get();

        Optional<ClientModel> clientOpt = clientRepo.findById(userId);
        if(clientOpt.isEmpty()) {
            return "Invalid client ID: " + userId;
        }
        ClientModel client = clientOpt.get();


        Node newNode = new Node(userId, value, parent);
        crdtAlgo.insertNode(parent, newNode);
        nodeRepo.save(newNode);
        nodeRepo.save(parent); 
        doc.addNode(newNode);
        doc.addClient(client);
        //clientRepo.save(client);
        docRepo.save(doc);
    
        return newNode.getCharacter() + " added";
    }

    @Override
    public String deleteCharacter (String nodeId) {
        Optional<Node> nodeOpt = nodeRepo.findById(nodeId);
        if (nodeOpt.isEmpty()) {
            return "Invalid parent ID: " + nodeId;
        }
        Node node = nodeOpt.get();
        node.markDeleted();
        nodeRepo.save(node);
        return node.getCharacter() + " deleted";
    }

    @Override
    public String uploadDocument(UUID documentId, String text) {
        Optional<DocumentModel> optional = docRepo.findById(documentId);
        if (optional.isPresent()) {
            DocumentModel doc = optional.get();
            //doc.setContent(text);
            //documentRepository.save(doc);
            return "Document updated successfully.";
        } else {
            DocumentModel newDoc = new DocumentModel();
            //newDoc.setId(documentId);
            //newDoc.setContent(text);
            //documentRepository.save(newDoc);
            return "Document created and saved successfully.";
        }
    }

    
    @Override
    public DocumentResponseDTO downloadDocument(UUID documentId) {
        return docRepo.findById(documentId)
            .map(doc -> {
                String textContent = buildDocumentText(doc); // flatten CRDT nodes to string
                List<CollaboratorDTO> collaborators = doc.getClients().stream()
                .map(c -> new CollaboratorDTO(c.getUsername(), c.getMode()))
                .toList();


                DocumentResponseDTO response = new DocumentResponseDTO();
                response.setDocumentId(doc.getUID());
                response.setEditCode(doc.getEditCode());
                response.setViewCode(doc.getViewCode());
                response.setCollaborators(collaborators);
                response.setTextContent(textContent);
                response.setRootNodeId(doc.getRoot().getUID());  // You forgot this
                return response;
            })
            .orElseThrow(() -> new RuntimeException("Document not found."));
    }


    private String buildDocumentText(DocumentModel doc) {
        return crdtAlgo.getDocumentText(doc.getRoot());
    }

    /////////////////Test/////////////////////
    /// 
    /* 
    @PostConstruct
    public void init() {
    ClientModel client = new ClientModel();
    client.setUsername("test_user");
    client.setCode("abc123");
    client.setMode(ClientModel.UserMode.EDITOR);
    clientRepo.save(client);
    */
}


