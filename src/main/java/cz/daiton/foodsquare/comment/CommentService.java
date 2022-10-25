package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    Comment get(Long id);

    List<Comment> getAll();

    List<Comment> getAllByRecipe(Long id);

    List<Comment> getAllByAppUser(Long id);

    String add(CommentDto commentDto, HttpServletRequest request) throws IncorrectUserException;

    String update(CommentDto commentDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;

    void deleteRecursively(Comment comment);

    Integer countByRecipe(Long id);

    Integer countLikes(Long id);

    Boolean isLikedByUser(Long id, HttpServletRequest request) throws IncorrectUserException;
}
