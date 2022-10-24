package cz.daiton.foodsquare.IO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root = Paths.get("img");
    Path users = Paths.get(root + "/users/");
    Path recipes = Paths.get(root + "/recipes/");

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
            if (!Files.exists(users)) {
                Files.createDirectory(users);
            }
            if (!Files.exists(recipes)) {
                Files.createDirectory(recipes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize resource folder.");
        }
    }

    @Override
    public String save(MultipartFile file, String type, Long id) {
        try {
            String pathToImage = id + "." + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            Path dir;
            if (type.equals("recipe")) {
                Files.copy(
                        file.getInputStream(),
                        recipes.resolve(pathToImage),
                        StandardCopyOption.REPLACE_EXISTING
                );
                dir = recipes;
            }
            else {
                Files.copy(
                        file.getInputStream(),
                        users.resolve(pathToImage),
                        StandardCopyOption.REPLACE_EXISTING
                );
                dir = users;
            }
            return dir.toString() + "\\" + pathToImage;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public void delete(Path path) {
        try {
            Files.delete(Paths.get(path.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
    }

    @Override
    public InputStream load(String filename, String type) throws FileNotFoundException {
        Path file;
        if (type.equals("recipe")) {
            file = recipes.resolve(filename);
        }
        else {
            file = users.resolve(filename);
        }
        return new FileInputStream(file.toString());
    }

    @Override
    public String getContentType(String filename, String type) {
        Path file;
        if (type.equals("recipe")) {
            file = recipes.resolve(filename);
        }
        else {
            file = users.resolve(filename);
        }
        String extension = file.toString().substring(file.toString().lastIndexOf(".") + 1);

        if (extension.equals("png")) {
            return MediaType.IMAGE_PNG_VALUE;
        }
        else if (extension.equals("jpeg") || extension.equals("jpg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        }
        else {
            return MediaType.IMAGE_GIF_VALUE;
        }
    }
}