package cmps211.example.texteditor.repository;

import org.springframework.data.repository.CrudRepository;

import cmps211.example.texteditor.models.ClientModel;

public interface ClientRepository extends  CrudRepository<ClientModel, Integer>{
    
}
