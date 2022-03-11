package cz.daiton.foodsquare.post.review;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review get(Long id) {
        return reviewRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public void add(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public void update(ReviewDto reviewDto, Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(NoSuchElementException::new);
        review.setHeader(reviewDto.getHeader());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
