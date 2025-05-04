package cmps211.example.texteditor.service.Interfaces;

public interface IUndoRedoService {
    String undo(int clientId);
    public String redo(int clientId);
}
