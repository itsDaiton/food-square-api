package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping(value = "api/v1/follows")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints for CRUD operations with follow relation.", name = "Follow Controller")
public class FollowController {

    private final FollowService followService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Returns a specific follow relation based on given parameter.")
    public Follow getFollow(@Parameter(description = "ID of the follow relation.", example = "1") @PathVariable Long id) {
        return followService.get(id);
    }

    @GetMapping()
    @Operation(summary = "Returns list of all follow relations in the application.")
    public List<Follow> getAllFollows() {
        return followService.getAll();
    }

    @GetMapping(value = "/user/{id}/followers")
    @Operation(summary = "Returns list of all followers of a specific user.")
    public List<Follow> getFollowersOfUser(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return followService.getAllFollowersOfUser(id);
    }

    @GetMapping(value = "/user/{id}/following")
    @Operation(summary = "Returns list of all following of a specific user.")
    public List<Follow> getFollowingOfUser(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return followService.getAllFollowingOfUser(id);
    }

    @GetMapping(value = "/{id}/follow-check")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Checks whether user follows a specific user.")
    public Boolean follows(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return followService.follows(id, request);
    }

    @PostMapping(value = "/follow")
    @Operation(summary = "Follows a user.")
    public ResponseEntity<?> followUser(@Valid @RequestBody FollowDto followDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(followService.add(followDto, request)));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Unfollows a user.")
    public ResponseEntity<?> unfollowUser(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(followService.delete(id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    NumberFormatException.class,
                    InvalidDataAccessApiUsageException.class,
                    HttpRequestMethodNotSupportedException.class,
                    DataIntegrityViolationException.class
            }
    )
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "Wrong request method. Please try again.";
        }
        else if (e instanceof NumberFormatException) {
            message = "Please enter a valid number.";
        }
        else if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Wrong format. Try again.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
