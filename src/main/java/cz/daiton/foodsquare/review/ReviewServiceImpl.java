package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Value("${server.timezone.offset}")
    private final long offset;

    @Override
    public Review get(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' was not found.")
        );
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public Review getByRecipe(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        AppUser appUser = appUserService.getUserFromCookie(request);

        return reviewRepository.findByAppUserAndRecipe(appUser, recipe).orElseThrow(
                () -> new NoSuchElementException("You did not review this recipe yet.")
        );
    }

    @Override
    public List<Review> getAllByAppUser(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
        return reviewRepository.findAllByAppUserOrderByUpdatedAtDesc(appUser);
    }

    @Override
    public List<Review> getAllByRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        return reviewRepository.findAllByRecipeOrderByUpdatedAtDesc(recipe);
    }

    @Override
    public String add(ReviewDto reviewDto, HttpServletRequest request) throws IncorrectUserException {
        Review review = new Review();
        AppUser appUser = appUserRepository.findById(reviewDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + reviewDto.getAppUser() + "' was not found.")
        );
        Recipe recipe = recipeRepository.findById(reviewDto.getRecipe()).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + reviewDto.getRecipe() + "' was not found.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {
            if (reviewRepository.existsByAppUserAndRecipe(appUser, recipe)) {
                throw new DataIntegrityViolationException("You already reviewed this recipe.");
            }
            review.setText(reviewDto.getText());
            review.setRating(reviewDto.getRating());
            review.setUpdatedAt(LocalDateTime.now().plusHours(offset));
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
                () -> new NoSuchElementException("Review with id: '" + id + "' was not found.")
        );

        if (appUserService.checkUser(review.getAppUser().getId(), request)) {
            review.setText(reviewDto.getText());
            review.setRating(reviewDto.getRating());
            review.setUpdatedAt(LocalDateTime.now().plusHours(offset));
            reviewRepository.save(review);

            return "Review has been successfully updated.";
        }
        return "There has been a error while trying to edit the review.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' was not found.")
        );

        if (appUserService.checkUser(review.getAppUser().getId(), request)) {

            if (!review.getLikes().isEmpty()) {
                List<AppUser> users = appUserRepository.findAll();
                for (AppUser a : users) {
                    a.getLikedReviews().remove(review);
                }
                review.getLikes().clear();
                reviewRepository.saveAndFlush(review);
                appUserRepository.saveAllAndFlush(users);
            }

            reviewRepository.deleteById(id);
            return "Review has been successfully deleted.";
        }
        return "There has been a error while trying to delete the review.";
    }

    @Override
    public void deleteRecursively(Review review) {
        if (!review.getLikes().isEmpty()) {
            List<AppUser> users = appUserRepository.findAll();
            for (AppUser a : users) {
                a.getLikedReviews().remove(review);
            }
            review.getLikes().clear();
            reviewRepository.saveAndFlush(review);
            appUserRepository.saveAllAndFlush(users);
        }
        reviewRepository.deleteById(review.getId());
    }

    @Override
    public Integer countByRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        return reviewRepository.countAllByRecipe(recipe);
    }

    @Override
    public Integer countLikes(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' was not found.")
        );
        return review.getLikes().size();
    }

    @Override
    public BigDecimal getAverageRating(Long id) {
        double sumOfRating = 0;
        int countOfReviews = 0;
        double result;

        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );

        List<Review> reviews = reviewRepository.findAllByRecipeOrderByUpdatedAtDesc(recipe);

        if (!reviews.isEmpty()) {
            for (Review r : reviews) {
                sumOfRating += r.getRating().doubleValue();
                countOfReviews++;
            }
            result = sumOfRating / countOfReviews;
        }
        else {
            result = 0;
        }

        return new BigDecimal(result).setScale(1, RoundingMode.HALF_EVEN);
    }

    @Override
    public Boolean containsReview(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        AppUser appUser = appUserService.getUserFromCookie(request);

        return reviewRepository.existsByAppUserAndRecipe(appUser, recipe);
    }

    @Override
    public Boolean isLikedByUser(Long id, HttpServletRequest request) throws IncorrectUserException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Review with id: '" + id + "' was not found.")
        );
        AppUser appUser = appUserService.getUserFromCookie(request);
        return review.getLikes().contains(appUser);
    }

    @Override
    public String deleteByRecipe(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        AppUser appUser = appUserService.getUserFromCookie(request);

        Review review = reviewRepository.findByAppUserAndRecipe(appUser, recipe).orElseThrow(
                () -> new NoSuchElementException("You did not review this recipe yet.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {

            if (!review.getLikes().isEmpty()) {
                List<AppUser> users = appUserRepository.findAll();
                for (AppUser a : users) {
                    a.getLikedReviews().remove(review);
                }
                review.getLikes().clear();
                reviewRepository.saveAndFlush(review);
                appUserRepository.saveAllAndFlush(users);
            }

            reviewRepository.deleteById(review.getId());
            return "You review for this recipe has been deleted successfully.";
        }
        return "There has been a error while trying to delete the review from the recipe.";
    }
}
