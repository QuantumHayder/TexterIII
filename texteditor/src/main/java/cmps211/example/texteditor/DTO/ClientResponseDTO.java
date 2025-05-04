package cmps211.example.texteditor.DTO;

import cmps211.example.texteditor.models.ClientModel.UserMode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientResponseDTO {
    private String username;
    private UserMode usermode;
    private int userUID;
    private String documentUID;
}
