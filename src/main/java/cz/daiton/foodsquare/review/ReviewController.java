package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
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
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = "get/{id}")
    public Review getReview(@PathVariable Long id) {
        return reviewService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Review> getReviews() {
        return reviewService.getAll();
    }

    @GetMapping(value = "/getByRecipe/{id}")
    public Review getReviewByRecipe(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return reviewService.getByRecipe(id, request);
    }

    @GetMapping(value = "/getCountByRecipe/{id}")
    public Integer countReviewsByRecipe(@PathVariable Long id) {
        return reviewService.countByRecipe(id);
    }

    @GetMapping(value = "/getAvgRating/{id}")
    public BigDecimal getAvgRatingOfRecipe(@PathVariable Long id) {
        return reviewService.getAverageRating(id);
    }

    @GetMapping(value = "/containsReview/{id}")
    public Boolean containsReview(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return reviewService.containsReview(id, request);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewDto reviewDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.add(reviewDto, request)));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateReview(@Valid @RequestBody ReviewDto reviewDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.update(reviewDto, id, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.delete(id, request)));
    }

    @DeleteMapping(value = "/deleteForUser/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteReviewByRecipeAndAppUser(@PathVariable Long id, HttpServletRequest request) throws Exception {
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

        if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Id's cannot null.";
        }
        else if (e instanceof HttpMessageNotReadableException) {
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
