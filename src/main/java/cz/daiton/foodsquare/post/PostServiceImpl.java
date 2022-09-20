package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.post.meal.Meal;
import cz.daiton.foodsquare.post.meal.MealRepository;
import cz.daiton.foodsquare.post.review.Review;
import cz.daiton.foodsquare.post.review.ReviewRepository;
import cz.daiton.foodsquare.post.thread.Thread;
import cz.daiton.foodsquare.post.thread.ThreadRepository;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final MealRepository mealRepository;
    private final ReviewRepository reviewRepository;
    private final ThreadRepository threadRepository;
    private final AppUserService appUserService;

    public PostServiceImpl(PostRepository postRepository, AppUserRepository appUserRepository, MealRepository mealRepository, ReviewRepository reviewRepository, ThreadRepository threadRepository, AppUserService appUserService) {
        this.postRepository = postRepository;
        this.appUserRepository = appUserRepository;
        this.mealRepository = mealRepository;
        this.reviewRepository = reviewRepository;
        this.threadRepository = threadRepository;
        this.appUserService = appUserService;
    }

    @Override
    public Post get(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Post with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public String add(PostDto postDto, HttpServletRequest request) throws IncorrectUserException{
        Post post = new Post();
        AppUser appUser = appUserRepository.findById(postDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + postDto.getAppUser() + "' does not exist.")
        );

        if (appUserService.checkUser(postDto.getAppUser(), request)) {

            checkMeal(postDto, post);
            checkReview(postDto, post);
            checkThread(postDto, post);

            post.setCreatedAt(LocalDateTime.now());
            post.setAppUser(appUser);
            postRepository.save(post);

            return "Post has been successfully created.";
        }
        return "There has been a error while trying to add the post.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Post with id: '" + id + "' does not exist. You cannot delete it.")
        );

        if (appUserService.checkUser(post.getAppUser().getId(), request)) {
            postRepository.deleteById(id);
            return "Post has been successfully deleted.";
        }
        return "There has been a error while trying to delete the post.";
    }

    private void checkMeal(PostDto postDto, Post post) {
        if (postDto.getMeal() != null) {
            Meal meal = mealRepository.findById(postDto.getMeal()).orElseThrow(
                    () -> new NoSuchElementException("Meal with id: '" + postDto.getMeal() + "' does not exist.")
            );
            if (postRepository.existsByMeal(meal)) {
                throw new DataIntegrityViolationException("Post with meal with id: '" + meal.getId() + "' already exists.");
            }
            else {
                post.setMeal(meal);
            }
        }
    }

    private void checkReview(PostDto postDto, Post post) {
        if (postDto.getReview() != null) {
            Review review = reviewRepository.findById(postDto.getReview()).orElseThrow(
                    () -> new NoSuchElementException("Review with id: '" + postDto.getReview() + "' does not exist.")
            );
            if (postRepository.existsByReview(review)) {
                throw new DataIntegrityViolationException("Post with review with id: '" + review.getId() + "' already exists.");
            }
            else {
                post.setReview(review);
            }
        }
    }

    private void checkThread(PostDto postDto, Post post) {
        if (postDto.getThread() != null) {
            Thread thread = threadRepository.findById(postDto.getThread()).orElseThrow(
                    () -> new NoSuchElementException("Thread with id: '" + postDto.getThread() + "' does not exist.")
            );
            if (postRepository.existsByThread(thread)) {
                throw new DataIntegrityViolationException("Post with thread with id: '" + thread.getId() + "' already exists.");
            }
            post.setThread(thread);
        }
    }
}
