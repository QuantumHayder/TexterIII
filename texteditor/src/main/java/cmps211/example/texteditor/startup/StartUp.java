/*package cmps211.example.texteditor.startup;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cmps211.example.texteditor.crdt.CRDTAlgorithm;
import cmps211.example.texteditor.crdt.Node;
import cmps211.example.texteditor.models.DocumentModel;
import cmps211.example.texteditor.repository.DocumentRepository;
import jakarta.annotation.PostConstruct;

@Component
public class StartUp {
     @Autowired
    private DocumentRepository docRepo;

    @Autowired
    private CRDTAlgorithm algo;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void loadTestData() {
        UUID docId = UUID.fromString("aea0353b-ba72-47d1-aa2c-baf7b736c1cf");

        DocumentModel doc = docRepo.findById(docId)
            .orElseThrow(() -> new RuntimeException("Document not found"));

        Node n1 = new Node(1, Instant.now(), 'a', null);
        Node n2 = new Node(1, Instant.now().plus(1, ChronoUnit.MILLIS), 'b', null);
        Node n3 = new Node(2, Instant.now(), 'c', null);

        algo.insertNode(doc.getRoot(), n1);
        algo.insertNode(n1, n2);
        algo.insertNode(doc.getRoot(), n3);

        System.out.println("Doc Text: " + algo.getDocumentText(doc.getRoot()));
    }
}*/
