package cmps211.example.texteditor.DTO;

import cmps211.example.texteditor.models.ClientModel.UserMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
    private String username;
    private UserMode usermode;
}
