package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final AppUserRepository appUserRepository;
    private final RecipeRepository recipeRepository;
    private final AppUserService appUserService;

    @Override
    public Review get(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public String add(ReviewDto reviewDto, HttpServletRequest request) throws IncorrectUserException {
        Review review = new Review();
        AppUser appUser = appUserRepository.findById(reviewDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + reviewDto.getAppUser() + "' does not exist.")
        );
        Recipe recipe = recipeRepository.findById(reviewDto.getRecipe()).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + reviewDto.getRecipe() + "' does not exist.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {
            review.setText(reviewDto.getText());
            review.setRating(reviewDto.getRating());
            review.setPathToImage(reviewDto.getPathToImage());
            review.setUpdatedAt(LocalDateTime.now());
            review.setAppUser(appUser);
            review.setRecipe(recipe);

            reviewRepository.save(review);
            return "Review has been successfully created.";
        }
        return "There has been a error while trying to add the review.";
    }

    @Override
    public String update(ReviewDto reviewDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' does not exist. You cannot edit it.")
        );

        if (appUserService.checkUser(review.getAppUser().getId(), request)) {
            review.setText(reviewDto.getText());
            review.setRating(reviewDto.getRating());
            review.setPathToImage(reviewDto.getPathToImage());
            review.setUpdatedAt(LocalDateTime.now());
            reviewRepository.save(review);

            return "Review has been successfully updated.";
        }
        return "There has been a error while trying to edit the review.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' does not exist. You cannot delete it.")
        );

        if (appUserService.checkUser(review.getAppUser().getId(), request)) {
            reviewRepository.deleteById(id);
            return "Review has been successfully deleted.";
        }
        return "There has been a error while trying to delete the review.";
    }
}
