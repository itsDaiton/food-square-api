package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FollowService {

    Follow get(Long id);

    List<Follow> getAll();

    List<Follow> getAllFollowersOfUser(Long id);

    List<Follow> getAllFollowingOfUser(Long id);

    String add(FollowDto followDto, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;

    Boolean follows(Long id, HttpServletRequest request) throws IncorrectUserException;

}
