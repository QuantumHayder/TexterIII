package com.example.client.Models;

import java.util.*;
import com.example.client.DTO.NodeDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrdtBuffer {
    private final List<NodeDTO> orderedNodes = new ArrayList<>();
    private final Map<String, Integer> nodeIndexMap = new HashMap<>();

    // NEW: store tree structure
    private final Map<String, List<NodeDTO>> childrenMap = new HashMap<>();
    private String rootId;

    /** 
     * Call once at startup with the server’s root node (dummy char '\0').
     */
    public void addRoot(NodeDTO root) {
        rootId = root.getNodeId();
        childrenMap.put(rootId, new ArrayList<>());
        rebuildFlat();
    }

    /**
     * Replace all children under parentId with the given list,
     * then rebuild the flat view.
     */
    public void insertChildren(String parentId, List<NodeDTO> childrenInOrder) {
        // 1. update the tree
        childrenMap.put(parentId, new ArrayList<>(childrenInOrder));
        // ensure every node has an entry in childrenMap
        for (NodeDTO c : childrenInOrder) {
            childrenMap.putIfAbsent(c.getNodeId(), new ArrayList<>());
        }
        // 2. rebuild the flat list
        rebuildFlat();
    }

    /** flatten from root in depth-first preorder */
    private void rebuildFlat() {
        orderedNodes.clear();
        nodeIndexMap.clear();
        traverse(rootId);
    }

    private void traverse(String nodeId) {
        List<NodeDTO> kids = childrenMap.get(nodeId);
        if (kids == null) return;
        for (NodeDTO child : kids) {
            int idx = orderedNodes.size();
            orderedNodes.add(child);
            nodeIndexMap.put(child.getNodeId(), idx);
            traverse(child.getNodeId());
        }
    }

    public List<NodeDTO> getOrderedNodes() {
        return Collections.unmodifiableList(orderedNodes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NodeDTO n : orderedNodes) {
            sb.append(n.getCharacter());
        }
        return sb.toString();
    }

    public boolean containsNode(String nodeId) {
        return nodeIndexMap.containsKey(nodeId);
    }
}
