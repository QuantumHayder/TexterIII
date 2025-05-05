package cmps211.example.texteditor.service.Interfaces;

import java.nio.file.Path;
import java.util.UUID;

public interface ITextFileExporter {
    public Path export(UUID documentId);
}
