package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.comment.CommentRepository;
import cz.daiton.foodsquare.exceptions.IncorrectTypeException;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.authentication.JwtUtils;
import cz.daiton.foodsquare.review.Review;
import cz.daiton.foodsquare.review.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final JwtUtils jwtUtils;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

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
    public String updateAdditionalInfo(AppUserDto appUserDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );

        if (checkUser(appUser.getId(), request)) {
            appUser.setFirstName(appUserDto.getFirstName());
            appUser.setLastName(appUserDto.getLastName());
            appUser.setPathToImage(appUserDto.getPathToImage());
            appUserRepository.save(appUser);

            return "Your profile has been updated.";
        }
        return "There has been a error while trying to edit your profile.";
    }

    @Override
    public String deleteProfilePicture(Long id, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );

        if (checkUser(appUser.getId(), request)) {
            appUser.setPathToImage(null);
            appUserRepository.save(appUser);

            return "Your profile picture has bee removed.";
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
}
