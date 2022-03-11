package cz.daiton.foodsquare.post.review;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
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
    public String addReview(@RequestBody Review review) {
        reviewService.add(review);
        return "Review has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updateReview(@RequestBody ReviewDto reviewDto, @PathVariable Long id) {
        reviewService.update(reviewDto, id);
        return "Review has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return "Review has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy
}
