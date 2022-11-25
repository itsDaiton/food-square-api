package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/comments")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints for CRUD operations with comments.", name = "Comment Controller")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Returns a specific comment based on given parameter.")
    public Comment getComment(@Parameter(description = "ID of the comment.", example = "1") @PathVariable Long id) {
        return commentService.get(id);
    }

    @GetMapping()
    @Operation(summary = "Returns list of all comments in the application.")
    public List<Comment> getComments() {
        return commentService.getAll();
    }

    @GetMapping(value = "/recipe/{id}")
    @Operation(summary = "Returns list of all comments from a specific recipe.")
    public List<Comment> getAllByRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return commentService.getAllByRecipe(id);
    }

    @GetMapping(value = "/{id}/likes")
    @Operation(summary = "Returns a count of likes of a specific comment.")
    public Integer getCommentLikes(
            @Parameter(description = "ID of the comment.", example = "1") @PathVariable Long id) {
        return commentService.countLikes(id);
    }

    @GetMapping(value = "/{id}/check-like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Checks whether user has liked a specific comment.")
    public Boolean isLikedByUser(
            @Parameter(description = "ID of the comment.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return commentService.isLikedByUser(id, request);
    }

    @GetMapping(value = "/user/{id}")
    @Operation(summary = "Returns list of all comments of a specific user.")
    public List<Comment> getAllByAppUser(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return commentService.getAllByAppUser(id);
    }

    @GetMapping(value = "/recipe/{id}/count")
    @Operation(summary = "Returns a count of comments for a specific recipe.")
    public Integer countCommentsByRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return commentService.countByRecipe(id);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Creates a comment.")
    public ResponseEntity<?> addComment(
            @Valid @RequestBody CommentDto commentDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(commentService.add(commentDto, request)));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Updates a comment.")
    public ResponseEntity<?> updateComment(
            @Valid @RequestBody CommentDto commentDto,
            @Parameter(description = "ID of the comment.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(commentService.update(commentDto, id, request)));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a comment.")
    public ResponseEntity<?> deleteComment(
            @Parameter(description = "ID of the comment.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
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
                    InvalidDataAccessApiUsageException.class,
                    HttpRequestMethodNotSupportedException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof NumberFormatException) {
            message = "Please enter a valid number.";
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "Wrong request method. Please try again.";
        }
        else {
            message = e.getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
