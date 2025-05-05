package cmps211.example.texteditor.service.Interfaces;

import cmps211.example.texteditor.DTO.ClientResponseDTO;
import cmps211.example.texteditor.models.ClientModel.UserMode;

public interface IClientService {
    ClientResponseDTO createClient(String username, UserMode usermode);
}
