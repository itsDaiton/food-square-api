package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.security.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    Post get(Long id);

    List<Post> getAll();

    void add(PostDto postDto);

    void update(PostDto postDto, Long id);

    void delete(Long id);

    String handleRequest(PostDto postDto, HttpServletRequest request) throws IncorrectUserException;
}
