package cz.daiton.foodsquare.like;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/likes")
@CrossOrigin
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping(value = "get/{id}")
    public Like getLike(@PathVariable Long id) {
        return likeService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Like> getLikes() {
        return likeService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addLike(@RequestBody LikeDto likeDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(likeService.add(likeDto, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteLike(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(likeService.delete(id, request)));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Values cannot be null. Please try again.";
        }
        else if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof NumberFormatException) {
            message = "Please enter a valid number as Id.";
        }
        else {
            message = e.getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
