package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.post.Post;
import cz.daiton.foodsquare.post.PostRepository;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppUserService appUserService;
    private final PostRepository postRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, AppUserService appUserService,PostRepository postRepository ) {
        this.reviewRepository = reviewRepository;
        this.appUserService = appUserService;
        this.postRepository = postRepository;
    }

    @Override
    public Review get(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Review add(ReviewDto reviewDto) {
        Review review = new Review();

        review.setHeader(reviewDto.getHeader());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());

        return reviewRepository.saveAndFlush(review);
    }

    @Override
    public String update(ReviewDto reviewDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' does not exist.")
        );
        Post post = postRepository.findByReview(review).orElseThrow(
                () -> new NoSuchElementException("Post with review with id: '" + id + "' has not been found.")
        );

        if (appUserService.checkUser(post.getAppUser().getId(), request)) {
            review.setHeader(reviewDto.getHeader());
            review.setContent(reviewDto.getContent());
            review.setRating(reviewDto.getRating());

            reviewRepository.save(review);
            return "Review has been successfully updated.";
        }
        return "There has been a error while trying to update the review.";
    }
}
