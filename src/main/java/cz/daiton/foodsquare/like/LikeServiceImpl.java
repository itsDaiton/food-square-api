package cz.daiton.foodsquare.like;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.post.Post;
import cz.daiton.foodsquare.post.PostRepository;
import cz.daiton.foodsquare.security.IncorrectUserException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;
    private final AppUserService appUserService;

    public LikeServiceImpl(LikeRepository likeRepository, AppUserRepository appUserRepository, PostRepository postRepository, AppUserService appUserService) {
        this.likeRepository = likeRepository;
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
        this.appUserService = appUserService;
    }

    @Override
    public Like get(Long id) {
        return likeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Like with id: '" + id + "' was not found.")
        );
    }

    @Override
    public List<Like> getAll() {
        return likeRepository.findAllByOrderByLikedAtDesc();
    }

    @Override
    public String add(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException {
        Like like = new Like();
        AppUser appUser = appUserRepository.findById(likeDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + likeDto.getAppUser() + "' does not exist.")
        );
        Post post = postRepository.findById(likeDto.getPost()).orElseThrow(
                () -> new NoSuchElementException("Post with id: '" + likeDto.getPost() + "' does not exist.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {
            if (likeRepository.existsByAppUserAndPost(appUser, post)) {
                throw new DataIntegrityViolationException("You already liked this post.");
            }
            else {
                like.setAppUser(appUser);
                like.setPost(post);
                like.setLikedAt(LocalDateTime.now());

                likeRepository.save(like);
                return "Post has been liked successfully.";
            }
        }
        return "There has been a error while trying to like the post.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Like like = likeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Like with id: '" + id + "' does not exist.")
        );

        if (appUserService.checkUser(like.getAppUser().getId(), request)) {
            likeRepository.deleteById(id);
            return "You are no longer liking this post.";
        }
        return "There has been a error while trying to delete the comment.";
    }
}
