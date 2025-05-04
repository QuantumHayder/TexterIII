package cmps211.example.texteditor.service.Interfaces;

import cmps211.example.texteditor.models.ClientModel.UserMode;

public interface IClientService {
    int createClient(String username, UserMode usermode);
}
