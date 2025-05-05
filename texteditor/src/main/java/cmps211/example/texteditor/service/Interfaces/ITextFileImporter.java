package cmps211.example.texteditor.service.Interfaces;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface ITextFileImporter {
     UUID importTextFile(MultipartFile file, String username) throws Exception;
}
