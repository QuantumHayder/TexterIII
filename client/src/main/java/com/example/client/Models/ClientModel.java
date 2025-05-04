package com.example.client.Models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientModel {
    
    private int UID;
    private String username;
    private UserMode mode;
    private String code; //code used for viewer/editor mode

    private DocumentModel document;

    public enum UserMode {
        VIEWER,
        EDITOR
    }
}
