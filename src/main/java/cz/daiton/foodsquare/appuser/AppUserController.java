package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.review.Review;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints for CRUD operations with users.", name = "User Controller")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Returns a specific user based on given parameter.")
    public AppUser getUser(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.get(id);
    }

    @GetMapping()
    @Operation(summary = "Returns list of all users in the application.")
    public List<AppUser> getAllUsers() {
        return appUserService.getAll();
    }

    @GetMapping(value = "/{id}/followers")
    @Operation(summary = "Returns list of all users that follow the specific user.")
    public List<AppUser> getFollowers(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.getFollowers(id);
    }

    @GetMapping(value = "/{id}/following")
    @Operation(summary = "Returns list of all users that the specific user is following.")
    public List<AppUser> getFollowing(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.getFollowing(id);
    }

    @GetMapping(value = "/random")
    @Operation(summary = "Returns list of 5 random users from the application.")
    public List<AppUser> get5RandomUsers() {
        return appUserRepository.find5RandomUsers();
    }

    @GetMapping(value = "/{id}/followers-count")
    @Operation(summary = "Returns a count of followers for a specific user.")
    public Integer countFollowers(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.countFollowers(id);
    }

    @GetMapping(value = "/{id}/following-count")
    @Operation(summary = "Returns a count of users that the specific user is following.")
    public Integer countFollowing(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.countFollowing(id);
    }

    @PutMapping(value = "/like")
    @Operation(summary = "Likes the desired content.")
    public ResponseEntity<?> like(
            @RequestBody LikeDto likeDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.like(likeDto, request)));
    }

    @PutMapping(value = "/unlike")
    @Operation(summary = "Unlikes the desired content.")
    public ResponseEntity<?> deleteLike(
            @RequestBody LikeDto likeDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.deleteLike(likeDto, request)));
    }

    @PutMapping(value = "/recipes/favorite")
    @Operation(summary = "Adds a recipe to favorites.")
    public ResponseEntity<?> favoriteRecipe(
            @RequestBody FavoriteDto favoriteDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.favoriteRecipe(favoriteDto, request)));
    }

    @PutMapping(value = "/recipes/unfavorite")
    @Operation(summary = "Removes a recipe from favorites.")
    public ResponseEntity<?> unfavoriteRecipe(
            @RequestBody FavoriteDto favoriteDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.unfavoriteRecipe(favoriteDto, request)));
    }

    @GetMapping(value = "/{id}/favorite-recipes")
    @Operation(summary = "Returns list of favorite recipes of a specific user.")
    public List<Recipe> getFavoriteRecipesOfUser(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.getFavoriteRecipesOfUser(id);
    }

    @GetMapping(value = "/{id}/liked-comments")
    @Operation(summary = "Returns list of liked comments of a specific user.")
    public List<Comment> getLikedCommentsOfUser(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.getLikedCommentsOfUser(id);
    }

    @GetMapping(value = "/{id}/liked-reviews")
    @Operation(summary = "Returns list of liked reviews of a specific user.")
    public List<Review> getLikedReviewsOfUser(@Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return appUserService.getLikedReviewsOfUser(id);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Updates user's personal information.")
    public ResponseEntity<?> updatePersonalInfo(
            @RequestBody AppUserDto appUserDto,
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.updatePersonalInfo(appUserDto, id, request)));
    }

    @PutMapping(value = "/{id}/image")
    @Operation(summary = "Adds a profile picture for a user.")
    public ResponseEntity<?> addProfilePicture(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id,
            @Parameter(description = "Multipart image file.") @RequestParam("image") MultipartFile file,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(appUserService.updateProfilePicture(id, file, request)));
    }

    @DeleteMapping(value = "/{id}/image")
    @Operation(summary = "Removes profile picture for a user.")
    public ResponseEntity<?> removeProfilePicture(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
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
            message = "Please enter a valid number.";
        }
        else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "Wrong argument type. Please try again.";
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
