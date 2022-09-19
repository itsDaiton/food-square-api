package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.security.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    Comment get(Long id);

    List<Comment> getAll();

    String add(CommentDto commentDto, HttpServletRequest request) throws IncorrectUserException;

    String update(CommentDto commentDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;
}
