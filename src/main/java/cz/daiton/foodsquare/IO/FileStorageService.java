package cz.daiton.foodsquare.IO;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileStorageService {

    void init();

    String save(MultipartFile file, String type, Long id);

    void deleteAll();

    void delete(Path path);

    String getContentType(String filename, String type);

    InputStream load(String filename, String type) throws FileNotFoundException;
}
