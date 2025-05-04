package cmps211.example.texteditor.models;

import java.util.*;

import cmps211.example.texteditor.crdt.Node;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
public class DocumentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@OneToMany(mappedBy = "documentId", fetch = FetchType.LAZY)
    private UUID UID;  //PK

    private String viewCode;
    private String editCode;

    //TODO consider whether adding the id or the whole object
    private int ownerId;  

    @OneToMany(mappedBy = "document", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClientModel> clients; 

    @OneToMany(mappedBy = "document", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Node> nodes; 

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_node_id")
    
    private Node root;


    public DocumentModel (int owner) {
        this.ownerId = owner;
    }
    public void addNode (Node newNode) {
        this.nodes.add(newNode);
    }

    public void addClient (ClientModel newclient) {
        this.clients.add(newclient);
    }
}