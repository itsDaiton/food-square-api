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
import cz.daiton.foodsquare.security.IncorrectUserException;
import cz.daiton.foodsquare.security.jwt.JwtUtils;
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
    private final JwtUtils jwtUtils;

    public PostServiceImpl(PostRepository postRepository, AppUserRepository appUserRepository, MealRepository mealRepository, ReviewRepository reviewRepository, ThreadRepository threadRepository, AppUserService appUserService, JwtUtils jwtUtils) {
        this.postRepository = postRepository;
        this.appUserRepository = appUserRepository;
        this.mealRepository = mealRepository;
        this.reviewRepository = reviewRepository;
        this.threadRepository = threadRepository;
        this.appUserService = appUserService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Post get(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Post with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public String add(PostDto postDto, HttpServletRequest request) throws IncorrectUserException{
        Post post = new Post();
        AppUser appUser = appUserRepository.findById(postDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + postDto.getAppUser() + "' does not exist.")
        );

        if (checkUser(postDto.getAppUser(), request)) {

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

        if (checkUser(post.getAppUser().getId(), request)) {
            postRepository.deleteById(id);
            return "Post has been successfully deleted.";
        }
        return "There has been a error while trying to delete the post.";
    }

    private Boolean checkUser(Long id, HttpServletRequest request) throws IncorrectUserException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            AppUser appUser = appUserService.findByUsername(username);

            if (appUser.getId().equals(id)) {
                return true;
            }
            else {
                throw new IncorrectUserException("You are not authorized to operate with other user's content.");
            }
        }
        else {
            throw new IncorrectUserException("There has been an error with your token, please make a new login request.");
        }
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
