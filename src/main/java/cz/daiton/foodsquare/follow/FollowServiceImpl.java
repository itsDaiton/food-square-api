package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;

    @Override
    public Follow get(Long id) {
        return followRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no record with id: '" + id + "' present in the database.")
        );
    }

    @Override
    public List<Follow> getAll() {
        return followRepository.findAll();
    }

    @Override
    public List<Follow> getAllFollowersOfUser(Long id) {
        return followRepository.findAllByFollowerId(id);
    }

    @Override
    public List<Follow> getAllFollowingOfUser(Long id) {
        return followRepository.findAllByFollowedId(id);
    }

    @Override
    public String add(FollowDto followDto, HttpServletRequest request) throws IncorrectUserException {
        Follow follow = new Follow();
        AppUser follower = appUserRepository.findById(followDto.getFollower()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + followDto.getFollower() + "' does not exist. They cannot follow anyone.")
        );
        AppUser followed = appUserRepository.findById(followDto.getFollowed()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + followDto.getFollowed() + "' does not exist. They cannot be followed by anyone.")
        );

        if (appUserService.checkUser(follower.getId(), request)) {
            if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
                throw new DataIntegrityViolationException("You already follow this user.");
            }

            if (follower.getId().equals(followed.getId())) {
                throw new DataIntegrityViolationException("You cannot follow yourself.");
            }

            follow.setFollower(follower);
            follow.setFollowed(followed);
            followRepository.save(follow);
            return "You are now following this user.";
        }

        return "There has been a error while trying to follow the user.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        AppUser followed = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        AppUser me = appUserService.getUserFromCookie(request);

        if (followed.getId().equals(me.getId())) {
            throw new DataIntegrityViolationException("You cannot follow yourself.");
        }

        Follow follow = followRepository.findAllByFollowerAndFollowed(me, followed).orElseThrow(
                () -> new NoSuchElementException("You do not follow this user.")
        );

        if (appUserService.checkUser(me.getId(), request)) {
            followRepository.deleteById(follow.getId());
            return "You are no longer following this user.";
        }
        return "There has been a error while trying to unfollow the user.";
    }

    @Override
    public Boolean follows(Long id, HttpServletRequest request) throws IncorrectUserException {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' does not exist.")
        );
        AppUser me = appUserService.getUserFromCookie(request);

        if (appUser.getId().equals(me.getId())) {
            throw new DataIntegrityViolationException("You cannot follow yourself.");
        }

        return followRepository.existsByFollowerAndFollowed(me, appUser);
    }
}
