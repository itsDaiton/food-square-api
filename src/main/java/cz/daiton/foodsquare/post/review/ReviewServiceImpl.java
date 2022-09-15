package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final AppUserRepository appUserRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, AppUserRepository appUserRepository) {
        this.reviewRepository = reviewRepository;
        this.appUserRepository = appUserRepository;
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
    public void add(ReviewDto reviewDto) {
        Review review = new Review();
        AppUser appUser =  appUserRepository.findById(reviewDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        review.setHeader(reviewDto.getHeader());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        review.setAppUser(appUser);

        reviewRepository.save(review);
    }

    @Override
    public void update(ReviewDto reviewDto, Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(NoSuchElementException::new);
        AppUser appUser =  appUserRepository.findById(reviewDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        review.setHeader(reviewDto.getHeader());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        review.setAppUser(appUser);

        reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Review findTopByAppUserOrderByIdDesc(AppUser appUser) {
        return reviewRepository.findTopByAppUserOrderByIdDesc(appUser).orElseThrow(NoSuchElementException::new);
    }
}
