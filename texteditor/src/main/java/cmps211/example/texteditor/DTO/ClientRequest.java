package cmps211.example.texteditor.DTO;

import cmps211.example.texteditor.models.ClientModel.UserMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {
    private String username;
    private UserMode usermode;
}
