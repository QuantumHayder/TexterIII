package cmps211.example.texteditor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;


import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.hamcrest.Matchers;

import cmps211.example.texteditor.models.ClientModel;
import cmps211.example.texteditor.models.DocumentModel;
import cmps211.example.texteditor.repository.ClientRepository;
import cmps211.example.texteditor.repository.DocumentRepository;
import cmps211.example.texteditor.repository.NodeRepository;
import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class TexterRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private NodeRepository nodeRepo;

    private UUID createdDocumentId;


    @BeforeEach
    public void setup() throws Exception {
        clientRepo.deleteAll();
        docRepo.deleteAll();
        nodeRepo.deleteAll();

        // Create owner client
        ClientModel client = new ClientModel();
        client.setUsername("test_user2");
        client.setMode(ClientModel.UserMode.EDITOR);
        client.setCode("123");
        client = clientRepo.save(client);

        // Create document via REST
        String json = "{ \"ownerId\": " + client.getUID() + " }";
        String docId = mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        createdDocumentId = UUID.fromString(docId);
    }


    @Test
    @Transactional
    public void testCreateDocument() throws Exception {
        Assertions.assertNotNull(createdDocumentId);
        DocumentModel doc = docRepo.findById(createdDocumentId).orElse(null);
        Assertions.assertNotNull(doc, "Document should be created");
        Assertions.assertEquals(1, doc.getClients().size(), "Document should have one client");
    }

    @Test
    @Transactional
    public void testRegisterToDocument() throws Exception {
        // Create another client to register
        ClientModel joiner = new ClientModel();
        joiner.setUsername("viewer1");
        joiner.setMode(ClientModel.UserMode.VIEWER);
        joiner.setCode("xyz");
        joiner = clientRepo.save(joiner);

        // Prepare request body with new client's ID
        String[] data = { String.valueOf(joiner.getUID()) };
        String jsonBody = new ObjectMapper().writeValueAsString(data);

        // Send POST to /register/{documentId}
        mockMvc.perform(post("/register/" + createdDocumentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk());

        // Assert: joiner is now linked to the document
        final int joinerId = joiner.getUID();
        DocumentModel updatedDoc = docRepo.findById(createdDocumentId).get();
        boolean found = updatedDoc.getClients().stream()
            .anyMatch(c -> c.getUID() == joinerId);

        Assertions.assertTrue(found, "Client should be registered to document");
    }

    
}
