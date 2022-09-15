package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto) {
        reviewService.add(reviewDto);
        AppUser appUser = appUserService.get(reviewDto.getAppUser());
        Review review = reviewService.findTopByAppUserOrderByIdDesc(appUser);
        return ResponseEntity
                .ok()
                .body(new PostContentResponse(
                        review.getId(),
                        appUser.getId(),
                        "Review has been successfully added."
                ));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateReview(@RequestBody ReviewDto reviewDto, @PathVariable Long id) {
        reviewService.update(reviewDto, id);
        return "Review has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "Review has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy
}
