package cz.daiton.foodsquare.like;

import cz.daiton.foodsquare.security.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LikeService {

    Like get(Long id);

    List<Like> getAll();

    String add(LikeDto likeDto, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;

}
