package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.exceptions.IncorrectTypeException;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.review.Review;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppUserService {

    AppUser get(Long id);

    List<AppUser> getAll();

    void register(AppUser appUser);

    String updatePersonalInfo(AppUserDto appUserDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String updateProfilePicture(Long id, MultipartFile file, HttpServletRequest request) throws IncorrectUserException;

    String deleteProfilePicture(Long id, HttpServletRequest request) throws IncorrectUserException;

    AppUser findByUsername(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean checkUser(Long id, HttpServletRequest request) throws IncorrectUserException;

    AppUser getUserFromCookie(HttpServletRequest request) throws IncorrectUserException;

    String like(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException, IncorrectTypeException;

    String deleteLike(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException, IncorrectTypeException;

    String favoriteRecipe(FavoriteDto favoriteDto, HttpServletRequest request) throws IncorrectUserException;

    String unfavoriteRecipe(FavoriteDto favoriteDto, HttpServletRequest request) throws IncorrectUserException;

    List<Recipe> getFavoriteRecipesOfUser(Long id);

    List<Review> getLikedReviewsOfUser(Long id);

    List<Comment> getLikedCommentsOfUser(Long id);

    List<AppUser> getFollowers(Long id);

    List<AppUser> getFollowing(Long id);

    Integer countFollowers(Long id);

    Integer countFollowing(Long id);
}
