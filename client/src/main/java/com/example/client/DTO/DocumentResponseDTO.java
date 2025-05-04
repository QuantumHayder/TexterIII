package com.example.client.DTO;

import java.util.List;
import java.util.UUID;

import org.springframework.data.util.Pair;

import com.example.client.Models.ClientModel.UserMode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO {
    private UUID docUID;
    private String viewCode;
    private String editCode;
    List<Pair<String, UserMode>> collaborators;
    private int ownerId;
    private String rootNodeId;
    private String docText;
}
