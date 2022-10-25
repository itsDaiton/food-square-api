package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.IO.FileStorageService;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.comment.CommentRepository;
import cz.daiton.foodsquare.exceptions.IncorrectTypeException;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.authentication.JwtUtils;
import cz.daiton.foodsquare.follow.Follow;
import cz.daiton.foodsquare.follow.FollowRepository;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import cz.daiton.foodsquare.review.Review;
import cz.daiton.foodsquare.review.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.*;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final JwtUtils jwtUtils;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final FollowRepository followRepository;
    private final FileStorageService fileStorageService;

    @Override
    public AppUser get(Long id) {
        return appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    @Override
    public void register(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public List<AppUser> getFollowers(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
        List<Follow> followList = followRepository.findAllByFollowedId(appUser.getId());
        List<AppUser> followers = new ArrayList<>();
        for (Follow f : followList) {
            followers.add(f.getFollower());
        }
        return followers;
    }

    @Override
    public List<AppUser> getFollowing(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
        List<Follow> followList = followRepository.findAllByFollowerId(appUser.getId());
        List<AppUser> following = new ArrayList<>();
        for (Follow f : followList) {
            following.add(f.getFollowed());
        }
        return following;
    }

    @Override
    public String updatePersonalInfo(AppUserDto appUserDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );

        if (checkUser(appUser.getId(), request)) {
            appUser.setFirstName(appUserDto.getFirstName());
            appUser.setLastName(appUserDto.getLastName());
            appUserRepository.save(appUser);

            return "Your personal info has been updated.";
        }
        return "There has been a error while trying to edit your profile.";
    }

    @Override
    public String updateProfilePicture(Long id, MultipartFile file, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
        try {
            if (checkUser(appUser.getId(), request)) {
                String contentType = file.getContentType();
                Set<String> types = new HashSet<>();
                types.add(MediaType.IMAGE_GIF_VALUE);
                types.add(MediaType.IMAGE_JPEG_VALUE);
                types.add(MediaType.IMAGE_PNG_VALUE);
                if (!types.contains(contentType)) {
                    throw new RuntimeException("Only images with format PNG, GIF or JPEG are allowed.");
                }
                String pathToImage = fileStorageService.save(file, "user", id);
                appUser.setPathToImage(pathToImage);
                appUserRepository.saveAndFlush(appUser);
                return "Your profile picture has been updated successfully.";
            }
            return "There has been a error while trying to edit your profile picture.";

        } catch (Exception e) {
            return "Could not update your profile picture. Error: " + e.getMessage();
        }
    }

    @Override
    public String deleteProfilePicture(Long id, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
        if (checkUser(appUser.getId(), request)) {
            if (appUser.getPathToImage() == null) {
                throw new RuntimeException("You do not have a profile picture set up yet.");
            }
            String pathToImage = appUser.getPathToImage();
            fileStorageService.delete(Paths.get(pathToImage));
            appUser.setPathToImage(null);
            appUserRepository.saveAndFlush(appUser);
            return "Your profile picture has been removed.";
        }
        return "There has been a error while trying to remove your profile picture.";
    }

    @Override
    public String like(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException, IncorrectTypeException {
        AppUser appUser = appUserRepository.findById(likeDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + likeDto.getAppUser() + "' does not exist.")
        );
        Review review;
        Comment comment;

        if (checkUser(appUser.getId(), request)) {
            if (likeDto.getType().equals("review")) {
                review = reviewRepository.findById(likeDto.getContent()).orElseThrow(
                        () -> new NoSuchElementException("Review with id: '" + likeDto.getContent() + "' does not exist.")
                );
                if (appUser.getLikedReviews().contains(review)) {
                    throw new DataIntegrityViolationException("You already liked this review.");
                }
                appUser.getLikedReviews().add(review);
                appUserRepository.save(appUser);
                return "Review has been liked successfully.";
            }
            else if(likeDto.getType().equals("comment")) {
                comment = commentRepository.findById(likeDto.getContent()).orElseThrow(
                        () -> new NoSuchElementException("Comment with id: '" + likeDto.getContent() + "' does not exist.")
                );
                if (appUser.getLikedComments().contains(comment)) {
                    throw new DataIntegrityViolationException("You already liked this comment.");
                }
                appUser.getLikedComments().add(comment);
                appUserRepository.save(appUser);
                return "Comment has been liked successfully.";
            }
            else {
                throw new IncorrectTypeException("Wrong type provided. Please enter a correct type.");
            }
        }
        return "There has been a error while trying to like the content.";
    }

    @Override
    public String deleteLike(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException, IncorrectTypeException {
        AppUser appUser = appUserRepository.findById(likeDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + likeDto.getAppUser() + "' does not exist.")
        );
        Review review;
        Comment comment;

        if (checkUser(appUser.getId(), request)) {
            if (likeDto.getType().equals("review")) {
                review = reviewRepository.findById(likeDto.getContent()).orElseThrow(
                        () -> new NoSuchElementException("Review with id: '" + likeDto.getContent() + "' does not exist.")
                );
                if (appUser.getLikedReviews().contains(review)) {
                    appUser.getLikedReviews().remove(review);
                    appUserRepository.save(appUser);
                    return "You are no longer liking this review.";
                }
                else {
                    throw new NoSuchElementException("You cannot delete like on a review which you did not liked before.");
                }
            }
            else if (likeDto.getType().equals("comment")) {
                comment = commentRepository.findById(likeDto.getContent()).orElseThrow(
                        () -> new NoSuchElementException("Comment with id: '" + likeDto.getContent() + "' does not exist.")
                );
                if (appUser.getLikedComments().contains(comment)) {
                    appUser.getLikedComments().remove(comment);
                    appUserRepository.save(appUser);
                    return "You are no longer liking this comment.";
                }
            }
            else {
                throw new IncorrectTypeException("Wrong type provided. Please enter a correct type.");
            }
        }
        return "There has been a error while trying to delete the like.";
    }

    @Override
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUserName(username).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Boolean existsByUserName(String username) {
        return appUserRepository.existsByUserName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }

    @Override
    public Boolean checkUser(Long id, HttpServletRequest request) throws IncorrectUserException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            AppUser appUser = findByUsername(username);

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

    @Override
    public AppUser getUserFromCookie(HttpServletRequest request) throws IncorrectUserException {
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            return findByUsername(username);
        }
        else {
            throw new IncorrectUserException("There has been an error with your token, please make a new login request.");
        }
    }

    @Override
    public String favoriteRecipe(FavoriteDto favoriteDto, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(favoriteDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + favoriteDto.getAppUser() + "' does not exist.")
        );
        Recipe recipe;

        if (checkUser(appUser.getId(), request)) {
            recipe = recipeRepository.findById(favoriteDto.getRecipe()).orElseThrow(
                    () -> new NoSuchElementException("Recipe with id: " + favoriteDto.getRecipe() + "' does not exist.")
            );
            if (appUser.getFavoriteRecipes().contains(recipe)) {
                throw new DataIntegrityViolationException("You already added this recipe to your favorites.");
            }
            appUser.getFavoriteRecipes().add(recipe);
            appUserRepository.save(appUser);
            return "Recipe has been successfully added to favorites.";
        }
        return "There has been a error while trying to favorite the recipe.";
    }

    @Override
    public String unfavoriteRecipe(FavoriteDto favoriteDto, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(favoriteDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + favoriteDto.getAppUser() + "' does not exist.")
        );
        Recipe recipe;

        if (checkUser(appUser.getId(), request)) {
            recipe = recipeRepository.findById(favoriteDto.getRecipe()).orElseThrow(
                    () -> new NoSuchElementException("Recipe with id: " + favoriteDto.getRecipe() + "' does not exist.")
            );
            if (appUser.getFavoriteRecipes().contains(recipe)) {
                appUser.getFavoriteRecipes().remove(recipe);
                appUserRepository.save(appUser);
                return "Recipe has been removed from favorites.";
            }
            else {
                throw new NoSuchElementException("You cannot unfavorite recipe which you did not added to favorites before.");
            }
        }
        return "There has been a error while trying to unfavorite the recipe.";
    }

    @Override
    public List<Recipe> getFavoriteRecipesOfUser(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        return recipeRepository.findAllByFavoritesOrderByUpdatedAtDesc(appUser);
    }

    @Override
    public List<Review> getLikedReviewsOfUser(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        return reviewRepository.findAllByLikes(appUser);
    }

    @Override
    public List<Comment> getLikedCommentsOfUser(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        return commentRepository.findAllByLikes(appUser);
    }

    @Override
    public Boolean containsFavorite(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' doest not exist.")
        );
        AppUser me = getUserFromCookie(request);

        return me.getFavoriteRecipes().contains(recipe);
    }

    @Override
    public Integer countFollowers(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        return followRepository.countAllByFollowed(appUser);
    }

    @Override
    public Integer countFollowing(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        return followRepository.countAllByFollower(appUser);
    }
}
