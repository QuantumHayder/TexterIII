package cmps211.example.texteditor.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cmps211.example.texteditor.models.DocumentModel;

@Repository
public interface DocumentRepository extends CrudRepository<DocumentModel, UUID> {
    
}
