package cmps211.example.texteditor.service.Implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmps211.example.texteditor.DTO.ClientResponseDTO;
import cmps211.example.texteditor.models.ClientModel;
import cmps211.example.texteditor.models.ClientModel.UserMode;
import cmps211.example.texteditor.repository.ClientRepository;
import cmps211.example.texteditor.service.Interfaces.IClientService;
@Service
public class ClientService implements IClientService{
    @Autowired
    private ClientRepository clientRepo;

    @Override
    public ClientResponseDTO createClient(String username, UserMode usermode) {
        ClientModel client = new ClientModel();
        client.setUsername(username);
        client.setMode(usermode);
        //client.setCode(code);
        //client.setMode(ClientModel.UserMode.valueOf(mode.toUpperCase()));
        clientRepo.save(client);
        System.out.println("Saved client with UID = " + client.getUID()); // ✅

        return new ClientResponseDTO(client.getUsername(), client.getMode(), client.getUID()); // return the generated client ID
    }
    
    
}
