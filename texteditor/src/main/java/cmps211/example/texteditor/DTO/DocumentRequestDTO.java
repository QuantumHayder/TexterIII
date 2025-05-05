package cmps211.example.texteditor.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDTO {
     //int clientUID;

    private int clientUID;
    private UUID documentId;
    private String textContent;


}
