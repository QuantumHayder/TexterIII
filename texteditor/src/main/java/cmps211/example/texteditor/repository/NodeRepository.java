package cmps211.example.texteditor.repository;


import org.springframework.data.repository.CrudRepository;

import cmps211.example.texteditor.crdt.Node;

public interface NodeRepository extends CrudRepository<Node, String>{
    
}
