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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

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

        post.setCreatedAt(LocalDateTime.now());
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

        post.setCreatedAt(LocalDateTime.now());
        post.setAppUser(appUser);

        postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        }
        else {
            throw new NoSuchElementException("This post does not exist. You cannot delete it.");
        }
    }

    @Override
    public String handleRequest(PostDto postDto, HttpServletRequest request) throws IncorrectUserException {
        String jwt = jwtUtils.getJwtFromCookies(request);

        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            AppUser appUser = appUserService.findByUsername(username);

            if (appUser.getId().equals(postDto.getAppUser())) {
                Meal meal = mealRepository.findById(postDto.getMeal()).orElseThrow(NoSuchElementException::new);
                if (postRepository.existsById(meal.getId())) {
                    throw new DataIntegrityViolationException("Post with this meal already exists.");
                }
                add(postDto);
                return "Post has been successfully added.";
            }
            else {
                throw new IncorrectUserException("You are not authorized for this action.");
            }
        }
        return "Your token is invalid.";
    }
}
