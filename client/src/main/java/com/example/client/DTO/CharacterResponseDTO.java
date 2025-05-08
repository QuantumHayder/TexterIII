package com.example.client.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterResponseDTO {
    String ParentId;
    List<NodeDTO> children;
}
