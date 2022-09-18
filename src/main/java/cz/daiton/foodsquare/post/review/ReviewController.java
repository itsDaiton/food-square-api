package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import cz.daiton.foodsquare.security.IncorrectUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/reviews")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;

    private final AppUserService appUserService;

    public ReviewController(ReviewService reviewService, AppUserService appUserService) {
        this.reviewService = reviewService;
        this.appUserService = appUserService;
    }

    @GetMapping(value = "get/{id}")
    public Review getReview(@PathVariable Long id) {
        return reviewService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Review> getAllReviews() {
        return reviewService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewDto reviewDto, HttpServletRequest request) throws Exception {
        Review review = reviewService.add(reviewDto);
        return ResponseEntity
                .ok()
                .body(new PostContentResponse(
                        review.getId(),
                        appUserService.getLocalUser(request).getId(),
                        "Review has been successfully added."
                ));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateReview(@Valid @RequestBody ReviewDto reviewDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        reviewService.update(reviewDto, id, request);
        return ResponseEntity
                .ok()
                .body(new MessageResponse(reviewService.update(reviewDto, id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
