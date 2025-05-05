package cmps211.example.texteditor.crdt;

import java.time.Instant; //for time stamp to be independent of local time zone and etc

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import cmps211.example.texteditor.models.DocumentModel;
@Data
@Entity
@Getter
@Setter 
@NoArgsConstructor
public class Node {
    @Id
    //TODO: Consider how will the id be generated
    private String UID; // userId (1,2,3, etc) + timestamp)
    private int userId;
    private Instant clock; //time of creation
    private char character; //the character carried by the node 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")  // FK column in DB
    private Node parent; 
    
    private boolean flag; //in case this is a deleted node, the flag would be 1, else 0

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> children = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentModel document;

    public Node(int userId, char value, Node parent) {
        this.clock = Instant.now(); 
        this.userId = userId;
        this.UID = userId + "-" + clock.toEpochMilli(); //toEpochMilli converts time into an integer instead of string (better for ids)
        this.character = value;
        this.parent = parent;
        this.flag = false; 
    }

    public Node(int userId, Instant time,char value, Node parent) {
        this.clock = time; 
        this.userId = userId;
        this.UID = userId + "-" + clock.toEpochMilli(); //toEpochMilli converts time into an integer instead of string (better for ids)
        this.character = value;
        this.parent = parent;
        this.flag = false; 
    }

    public boolean isDeleted() {
        return this.flag;
    }
    
    public void markDeleted() {
        this.flag = true;
    }
    
    public int compareTo(Node other) {
        return this.clock.compareTo(other.clock); //if <0 "this.node" was created before incoming node, could also return 0 or >0
    }

    public void addChild(Node newNode)
    {
        if (!children.contains(newNode))
        { 
            children.add(newNode);
            newNode.setParent(this);
        }
    }
    
}
