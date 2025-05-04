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
    private String textContent;
    private String editCode;
    private String viewCode;
    private List<Pair<String, UserMode>> collaborators = new ArrayList<>();

    public void addCollaborators (String username, UserMode usermode) {
        collaborators.add(Pair.of(username,usermode));
    }
}