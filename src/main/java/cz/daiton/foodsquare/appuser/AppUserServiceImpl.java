package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.authentication.JwtUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final JwtUtils jwtUtils;

    public AppUserServiceImpl(AppUserRepository appUserRepository, JwtUtils jwtUtils) {
        this.appUserRepository = appUserRepository;
        this.jwtUtils = jwtUtils;
    }

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
    public AppUser getLocalUser(HttpServletRequest request) throws IncorrectUserException{
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            return findByUsername(username);
        }
        else {
            throw new IncorrectUserException("There has been an error with your token, please make a new login request.");
        }
    }
}
