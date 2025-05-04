package com.example.client.Models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Node {
    private String UID; // userId (1,2,3, etc) + timestamp)
    private int userId;
    private Instant clock; //time of creation
    private char character; //the character carried by the node
    private String parentUID; 
    private boolean flag; //in case this is a deleted node, the flag would be 1, else 0
    private String documentId;

    public Node(int userId, char value, String parent) {
        this.clock = Instant.now(); 
        this.userId = userId;
        this.UID = userId + "-" + clock.toEpochMilli(); //toEpochMilli converts time into an integer instead of string (better for ids)
        this.character = value;
        this.parentUID = parent;
        this.flag = false; 
    }

    public Node(int userId, Instant time,char value, String parent) {
        this.clock = time; 
        this.userId = userId;
        this.UID = userId + "-" + clock.toEpochMilli(); //toEpochMilli converts time into an integer instead of string (better for ids)
        this.character = value;
        this.parentUID = parent;
        this.flag = false; 
    }

    public boolean isDeleted() {
        return this.flag;
    }
    
}
