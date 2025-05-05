package cmps211.example.texteditor.DTO;

import cmps211.example.texteditor.models.ClientModel.UserMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDTO {
    private String username;
    private UserMode mode;
}
