package cz.daiton.foodsquare.review;

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/reviews")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints for CRUD operations with reviews.", name = "Review Controller")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = "{id}")
    @Operation(summary = "Returns a specific review based on given parameter.")
    public Review getReview(@Parameter(description = "ID of the review.", example = "1") @PathVariable Long id) {
        return reviewService.get(id);
    }

    @GetMapping()
    @Operation(summary = "Returns list of all reviews in the application.")
    public List<Review> getReviews() {
        return reviewService.getAll();
    }

    @GetMapping(value = "/recipe/{id}/my-review")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Returns a review for a specific user on a recipe.")
    public Review getReviewByRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return reviewService.getByRecipe(id, request);
    }

    @GetMapping(value = "/user/{id}")
    @Operation(summary = "Returns all reviews for a specific user.")
    public List<Review> getAllByAppUser(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return reviewService.getAllByAppUser(id);
    }

    @GetMapping(value = "/{id}/likes")
    @Operation(summary = "Returns a count of likes of a specific review.")
    public Integer getReviewLikes(
            @Parameter(description = "ID of the review.", example = "1") @PathVariable Long id) {
        return reviewService.countLikes(id);
    }

    @GetMapping(value = "/{id}/like-check")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Checks whether user has liked a specific review.")
    public Boolean isLikedByUser(
            @Parameter(description = "ID of the review.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return reviewService.isLikedByUser(id, request);
    }

    @GetMapping(value = "/recipe/{id}")
    @Operation(summary = "Returns all reviews of a specific review.")
    public List<Review> getAllByRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return reviewService.getAllByRecipe(id);
    }

    @GetMapping(value = "/recipe/{id}/count")
    @Operation(summary = "Returns a count of all reviews of a recipe.")
    public Integer countReviewsByRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return reviewService.countByRecipe(id);
    }

    @GetMapping(value = "/recipe/{id}/avg-rating")
    @Operation(summary = "Returns a average review rating for a recipe.")
    public BigDecimal getAvgRatingOfRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return reviewService.getAverageRating(id);
    }

    @GetMapping(value = "/recipe/{id}/my-review-check")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Checks whether user has reviewed a specific recipe.")
    public Boolean containsReview(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return reviewService.containsReview(id, request);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Adds a review.")
    public ResponseEntity<?> addReview(
            @Valid @RequestBody ReviewDto reviewDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.add(reviewDto, request)));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Updates a review.")
    public ResponseEntity<?> updateReview(
            @Valid @RequestBody ReviewDto reviewDto,
            @Parameter(description = "ID of the review.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.update(reviewDto, id, request)));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a review.")
    public ResponseEntity<?> deleteReview(
            @Parameter(description = "ID of the review.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.delete(id, request)));
    }

    @DeleteMapping(value = "/user/{id}")
    @Operation(summary = "Deletes a review for a specific user from a specific recipe.")
    public ResponseEntity<?> deleteReviewByRecipeAndAppUser(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.deleteByRecipe(id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    NumberFormatException.class,
                    InvalidDataAccessApiUsageException.class,
                    DataIntegrityViolationException.class
            })
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
        else {
            message = e.getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
