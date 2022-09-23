package cz.daiton.foodsquare.appuser;

import cz.daiton.foodsquare.exceptions.IncorrectTypeException;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppUserService {

    AppUser get(Long id);

    List<AppUser> getAll();

    void register(AppUser appUser);

    String updateAdditionalInfo(AppUserDto appUserDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String deleteProfilePicture(Long id, HttpServletRequest request) throws IncorrectUserException;

    AppUser findByUsername(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean checkUser(Long id, HttpServletRequest request) throws IncorrectUserException;

    String like(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException, IncorrectTypeException;

    String deleteLike(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException, IncorrectTypeException;
}
