package cmps211.example.texteditor.DTO;

import cmps211.example.texteditor.models.ClientModel.UserMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseDTO {
    private String username;
    private UserMode usermode;
    private int userUID;
}
