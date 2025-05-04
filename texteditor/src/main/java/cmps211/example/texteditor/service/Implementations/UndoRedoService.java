package cmps211.example.texteditor.service.Implementations;

import org.springframework.beans.factory.annotation.Autowired;

import cmps211.example.texteditor.crdt.CRDTAlgorithm;
import cmps211.example.texteditor.crdt.Node;
import cmps211.example.texteditor.repository.NodeRepository;
import cmps211.example.texteditor.service.Interfaces.IUndoRedoService;

public class UndoRedoService implements IUndoRedoService {
    
    @Autowired
    private CRDTAlgorithm crdtAlgo; 
    @Autowired
    private NodeRepository nodeRepo;

    @Override
    public String undo(int clientId) {
        Node node = crdtAlgo.undo(clientId);   
        if(node == null)
            return "No actions were found for that clientId";
        
        node.setFlag(!node.isDeleted());
        crdtAlgo.getRedoStack().add(node);
        nodeRepo.save(node);
        return "Last action is undone for user" + node.getUserId();
    }

    @Override
    public String redo(int clientId) {
        Node node = crdtAlgo.redo(clientId);   
        if(node == null)
            return "No actions were found for that clientId";
      
        node.setFlag(!node.isDeleted());
        crdtAlgo.getUndoStack().add(node);
        nodeRepo.save(node);
        return "Last action is redone for user" + node.getUserId();
    }


}
