package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.post.meal.Meal;
import cz.daiton.foodsquare.post.meal.MealRepository;
import cz.daiton.foodsquare.post.review.Review;
import cz.daiton.foodsquare.post.review.ReviewRepository;
import cz.daiton.foodsquare.post.thread.Thread;
import cz.daiton.foodsquare.post.thread.ThreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final MealRepository mealRepository;
    private final ReviewRepository reviewRepository;
    private final ThreadRepository threadRepository;

    public PostServiceImpl(PostRepository postRepository, AppUserRepository appUserRepository, MealRepository mealRepository, ReviewRepository reviewRepository, ThreadRepository threadRepository) {
        this.postRepository = postRepository;
        this.appUserRepository = appUserRepository;
        this.mealRepository = mealRepository;
        this.reviewRepository = reviewRepository;
        this.threadRepository = threadRepository;
    }

    @Override
    public Post get(Long id) {
        return postRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public void add(PostDto postDto) {
        Post post = new Post();
        AppUser appUser = appUserRepository.findById(postDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        if (postDto.getMeal() != null) {
            Meal meal = mealRepository.findById(postDto.getMeal()).orElseThrow(NoSuchElementException::new);
            post.setMeal(meal);
        }

        if (postDto.getReview() != null) {
            Review review = reviewRepository.findById(postDto.getReview()).orElseThrow(NoSuchElementException::new);
            post.setReview(review);
        }

        if (postDto.getThread() != null) {
            Thread thread = threadRepository.findById(postDto.getThread()).orElseThrow(NoSuchElementException::new);
            post.setThread(thread);
        }

        post.setCreatedAt(postDto.getCreatedAt());
        post.setAppUser(appUser);

        postRepository.save(post);
    }

    @Override
    public void update(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(NoSuchElementException::new);
        AppUser appUser = appUserRepository.findById(postDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        if (postDto.getMeal() != null) {
            Meal meal = mealRepository.findById(postDto.getMeal()).orElseThrow(NoSuchElementException::new);
            post.setMeal(meal);
        }

        if (postDto.getReview() != null) {
            Review review = reviewRepository.findById(postDto.getReview()).orElseThrow(NoSuchElementException::new);
            post.setReview(review);
        }

        if (postDto.getThread() != null) {
            Thread thread = threadRepository.findById(postDto.getThread()).orElseThrow(NoSuchElementException::new);
            post.setThread(thread);
        }

        post.setCreatedAt(postDto.getCreatedAt());
        post.setAppUser(appUser);

        postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
