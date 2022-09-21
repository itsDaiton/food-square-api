package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/follows")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping(value = "get/{id}")
    public Follow getFollow(@PathVariable Long id) {
        return followService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Follow> getAllFollows() {
        return followService.getAll();
    }

    @GetMapping(value = "/followers/{id}")
    public List<Follow> getFollowersOfUser(@PathVariable Long id) {
        return followService.getAllFollowersOfUser(id);
    }

    @GetMapping(value = "/following/{id}")
    public List<Follow> getFollowingOfUser(@PathVariable Long id) {
        return followService.getAllFollowingOfUser(id);
    }

    @PostMapping(value = "/follow")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> followUser(@Valid @RequestBody FollowDto followDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(followService.add(followDto, request)));
    }

    @DeleteMapping(value = "/unfollow/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(followService.delete(id, request)));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
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
