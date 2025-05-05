package cmps211.example.texteditor.DTO;

import java.util.*;

import org.springframework.data.util.Pair;

import cmps211.example.texteditor.models.ClientModel.UserMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDTO {
    private UUID documentId;
    private String viewCode;
    private String editCode;
    private List<CollaboratorDTO> collaborators = new ArrayList<>();
    private String rootNodeId;
    private String textContent;
    public void addCollaborators (CollaboratorDTO member) {
        collaborators.add(member);
    }
}