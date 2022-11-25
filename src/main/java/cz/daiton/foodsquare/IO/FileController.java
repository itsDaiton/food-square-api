package cz.daiton.foodsquare.IO;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "img")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints returning specific images.", name = "File Controller")
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping(value = "/recipes/{filename}")
    @Operation(summary = "Returns a recipe image based on given parameter.")
    public void getRecipeImage(
            @Parameter(description = "Path to image.", example = "1.jpg") @PathVariable String filename,
            HttpServletResponse response) throws IOException {
        InputStream img = fileStorageService.load(filename, "recipe");
        String contentType = fileStorageService.getContentType(filename, "recipe");
        response.setContentType(contentType);
        StreamUtils.copy(img, response.getOutputStream());
        img.close();
    }

    @GetMapping(value = "/users/{filename}")
    @Operation(summary = "Returns a user profile picture based on given parameter.")
    public void getUserImage(
            @Parameter(description = "Path to image.", example = "1.png") @PathVariable String filename,
            HttpServletResponse response) throws IOException {
        InputStream img = fileStorageService.load(filename, "user");
        String contentType = fileStorageService.getContentType(filename, "user");
        response.setContentType(contentType);
        StreamUtils.copy(img, response.getOutputStream());
        img.close();
    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message = "File not found.";
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
