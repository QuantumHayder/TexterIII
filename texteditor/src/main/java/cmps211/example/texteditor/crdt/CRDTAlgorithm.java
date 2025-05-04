package cmps211.example.texteditor.crdt;


import cmps211.example.texteditor.models.ClientModel;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import org.springframework.stereotype.Component;

@Setter
@Getter
@Component  //to help spring boot inject beans
public class CRDTAlgorithm {

    private final LinkedList<Node> undoStack = new LinkedList<>();
    private final LinkedList<Node> redoStack = new LinkedList<>();
   
    //used for inserting a new char after a specific char for the current doc and user
    public void insertNode(Node parent, Node child) {
        child.setParent(parent);
        parent.addChild(child);
        sortChildren(parent);
        undoStack.add(child);
    };

    private void sortChildren(Node parent) {
        List<Node> children = parent.getChildren();

        // Sort first by timestamp descending, then by user ID ascending if timestamps are equal
        children.sort(Comparator.comparing(Node::getClock, Comparator.reverseOrder())
                                .thenComparing(Node::getUID));
    }

    public void deleteNode(Node node) {
        node.markDeleted();
        undoStack.add(node);
    }

    public Node getNodeById(Node root, String UID) {
        if (root == null) {
            return null;
        }
        
        if (root.getUID().equals(UID)) {
            return root;
        }
        
        if (root.getChildren() != null) {
            for (Node child : root.getChildren()) {
                Node result = getNodeById(child, UID); 
                if (result != null) {
                    return result; 
                }
            }
        }
    
        return null;
    }
    
    //used when broadcasting(sending the newly created node to all other users)
    public void merge(Node root, Node newNode) {
        Node OldParent = newNode.getParent();
        Node NewParent = getNodeById(root, OldParent.getUID());
        if (NewParent != null) {
           insertNode(NewParent, newNode);
           sortChildren(NewParent);
        }
    
    }

    
    // Traverse the tree starting from the root and return the visible characters as a string
    public String getDocumentText(Node root) {
        StringBuilder result = new StringBuilder();
        traverseAndBuild(root, result);
        return result.toString();
    }

    // Recursive helper method to build the text
    private void traverseAndBuild(Node node, StringBuilder result) {
        if (node == null) return;

        // Skip the root node if it’s just a dummy holder (like a virtual head node)
        if (node.getCharacter() != '\0' && !node.isDeleted()) {
            result.append(node.getCharacter());
        }

        List<Node> children = new ArrayList<>(node.getChildren());

        for (Node child : children) {
            traverseAndBuild(child, result);
        }
    }

    public Node undo(int userId) {
        ListIterator<Node> it = undoStack.listIterator(undoStack.size()); // start at the end

        while (it.hasPrevious()) {
            Node node = it.previous();
            int nodeUserId = node.getUserId();
            if (userId == nodeUserId) {
                it.remove();
                return node;
            }
        }
        return null;
    }

    public Node redo(int userId) {
        ListIterator<Node> it = redoStack.listIterator(redoStack.size());

        while (it.hasPrevious()) {
            Node node = it.previous();
            int nodeUserId = node.getUserId();
            if (userId == nodeUserId) {
                it.remove();
                return node;
            }
        }
        return null;
    }
}
