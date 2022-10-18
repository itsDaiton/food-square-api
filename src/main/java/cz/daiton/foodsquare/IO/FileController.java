package cz.daiton.foodsquare.IO;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StreamUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "img")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping(value = "/recipes/{filename}")
    public void getRecipeImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        InputStream img = fileStorageService.load(filename, "recipe");
        String contentType = fileStorageService.getContentType(filename, "recipe");
        response.setContentType(contentType);
        StreamUtils.copy(img, response.getOutputStream());
    }

    @GetMapping(value = "/users/{filename}")
    public void getUserImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        InputStream img = fileStorageService.load(filename, "user");
        String contentType = fileStorageService.getContentType(filename, "user");
        response.setContentType(contentType);
        StreamUtils.copy(img, response.getOutputStream());
    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message = "File not found.";
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
