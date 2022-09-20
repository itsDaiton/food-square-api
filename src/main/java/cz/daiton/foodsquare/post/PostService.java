package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    Post get(Long id);

    List<Post> getAll();

    String add(PostDto postDto, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;

}
