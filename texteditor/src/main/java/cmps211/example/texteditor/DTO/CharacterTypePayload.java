package cmps211.example.texteditor.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterTypePayload {
    private int userId;
    private char character;
    private String parentNodeId;
}
