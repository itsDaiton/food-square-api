package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import cz.daiton.foodsquare.security.IncorrectUserException;
import cz.daiton.foodsquare.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reviews")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;

    private final AppUserService appUserService;

    private final JwtUtils jwtUtils;

    public ReviewController(ReviewService reviewService, AppUserService appUserService, JwtUtils jwtUtils) {
        this.reviewService = reviewService;
        this.appUserService = appUserService;
        this.jwtUtils = jwtUtils;
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
    public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request) throws IncorrectUserException {
        Review review = reviewService.add(reviewDto);

        String jwt = jwtUtils.getJwtFromCookies(request);

        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            AppUser appUser = appUserService.findByUsername(username);
            return ResponseEntity
                    .ok()
                    .body(new PostContentResponse(
                            review.getId(),
                            appUser.getId(),
                            "Review has been successfully added."
                    ));
        }
        else {
            throw new IncorrectUserException("You cannot do this. You are not the same user.");
        }
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
