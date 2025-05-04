package com.example.client.Models;

import java.util.UUID;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentModel {
    
    private UUID UID;  //PK
    private int ownerId;  
    private List<ClientModel> clients; 
    private List<Node> nodes; 
    private String rootUID;

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
