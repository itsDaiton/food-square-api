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
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    @GetMapping(value = "get/{id}")
    public AppUser getUser(@PathVariable Long id) {
        return appUserService.get(id);
    }


    @GetMapping(value = "/getAll")
    public List<AppUser> getAllUsers() {
        return appUserService.getAll();
    }

    @GetMapping(value = "/getFollowers/{id}")
    public List<AppUser> getFollowers(@PathVariable Long id) {
        return appUserService.getFollowers(id);
    }

    @GetMapping(value = "/getFollowing/{id}")
    public List<AppUser> getFollowing(@PathVariable Long id) {
        return appUserService.getFollowing(id);
    }

    @GetMapping(value = "/get5Random")
    public List<AppUser> get5RandomUsers() {
        return appUserRepository.find5RandomUsers();
    }

    @GetMapping(value = "/countFollowers/{id}")
    public Integer countFollowers(@PathVariable Long id) {
        return appUserService.countFollowers(id);
    }

    @GetMapping(value = "/countFollowing/{id}")
    public Integer countFollowing(@PathVariable Long id) {
        return appUserService.countFollowing(id);
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

    @GetMapping(value = "/checkFavorite/{id}")
    public Boolean checkForFavorite(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return appUserService.containsFavorite(id, request);
    }

    @PutMapping(value = "/updateInfo/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePersonalInfo(@RequestBody AppUserDto appUserDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.updatePersonalInfo(appUserDto, id, request)));
    }

    @PutMapping(value = "/addImage/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addProfilePicture(@PathVariable Long id, @RequestParam("image") MultipartFile file, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.updateProfilePicture(id, file, request)));
    }

    @DeleteMapping(value = "/deleteImage/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeProfilePicture(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.deleteProfilePicture(id, request)));
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
        else if (e instanceof MissingServletRequestPartException) {
            message = "Please choose an image.";
        }
        else {
            message = e.getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
