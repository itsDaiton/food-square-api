package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
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
@RequestMapping(value = "api/v1/follows")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping(value = "/{id}")
    public Follow getFollow(@PathVariable Long id) {
        return followService.get(id);
    }

    @GetMapping()
    public List<Follow> getAllFollows() {
        return followService.getAll();
    }

    @GetMapping(value = "/user/{id}/followers")
    public List<Follow> getFollowersOfUser(@PathVariable Long id) {
        return followService.getAllFollowersOfUser(id);
    }

    @GetMapping(value = "/user/{id}/following")
    public List<Follow> getFollowingOfUser(@PathVariable Long id) {
        return followService.getAllFollowingOfUser(id);
    }

    @GetMapping(value = "/{id}/follow-check")
    @PreAuthorize("isAuthenticated()")
    public Boolean follows(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return followService.follows(id, request);
    }

    @PostMapping(value = "/follow")
    public ResponseEntity<?> followUser(@Valid @RequestBody FollowDto followDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(followService.add(followDto, request)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id, HttpServletRequest request) throws Exception {
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
                    HttpRequestMethodNotSupportedException.class
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
