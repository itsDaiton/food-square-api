package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/comments")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "get/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Comment> getComments() {
        return commentService.getAll();
    }

    @GetMapping(value = "/getAllByRecipe/{id}")
    public List<Comment> getAllByRecipe(@PathVariable Long id) {
        return commentService.getAllByRecipe(id);
    }

    @GetMapping(value = "/getLikes/{id}")
    public Integer getCommentLikes(@PathVariable Long id) {
        return commentService.countLikes(id);
    }

    @GetMapping(value = "/isLikedByUser/{id}")
    @PreAuthorize("hasRole('USER')")
    public Boolean isLikedByUser(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return commentService.isLikedByUser(id, request);
    }

    @GetMapping(value = "/getAllByUser/{id}")
    public List<Comment> getAllByAppUser(@PathVariable Long id) {
        return commentService.getAllByAppUser(id);
    }

    @GetMapping(value = "getCountByRecipe/{id}")
    public Integer countCommentsByRecipe(@PathVariable Long id) {
        return commentService.countByRecipe(id);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(commentService.add(commentDto, request)));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(commentService.update(commentDto, id, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(commentService.delete(id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    NumberFormatException.class,
                    InvalidDataAccessApiUsageException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Id's cannot null.";
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
