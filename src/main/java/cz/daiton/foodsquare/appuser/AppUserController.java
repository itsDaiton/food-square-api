package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.review.Review;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping(value = "get/{id}")
    public AppUser getUser(@PathVariable Long id) {
        return appUserService.get(id);
    }


    @GetMapping(value = "/getAll")
    public List<AppUser> getAllUsers() {
        return appUserService.getAll();
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateInfo(@RequestBody AppUserDto appUserDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.updateAdditionalInfo(appUserDto, id, request)));
    }

    @PutMapping(value = "/updatePicture/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeProfilePicture(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.deleteProfilePicture(id, request)));
    }

    @PutMapping(value = "/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> like(@RequestBody LikeDto likeDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.like(likeDto, request)));
    }

    @PutMapping(value = "/deleteLike")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteLike(@RequestBody LikeDto likeDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.deleteLike(likeDto, request)));
    }

    @PutMapping(value = "/favoriteRecipe")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> favoriteRecipe(@RequestBody FavoriteDto favoriteDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.favoriteRecipe(favoriteDto, request)));
    }

    @PutMapping(value = "/unfavoriteRecipe")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unfavoriteRecipe(@RequestBody FavoriteDto favoriteDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.unfavoriteRecipe(favoriteDto, request)));
    }

    @GetMapping(value = "/getFavoriteRecipes/{id}")
    public List<Recipe> getFavoriteRecipesOfUser(@PathVariable Long id) {
        return appUserService.getFavoriteRecipesOfUser(id);
    }

    @GetMapping(value = "/getLikedComments/{id}")
    public List<Comment> getLikedCommentsOfUser(@PathVariable Long id) {
        return appUserService.getLikedCommentsOfUser(id);
    }

    @GetMapping(value = "/getLikedReviews/{id}")
    public List<Review> getLikedReviewsOfUser(@PathVariable Long id) {
        return appUserService.getLikedReviewsOfUser(id);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof NumberFormatException) {
            message = "Please enter a valid number as Id.";
        }
        else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "This is not valid ID. Please try again.";
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
