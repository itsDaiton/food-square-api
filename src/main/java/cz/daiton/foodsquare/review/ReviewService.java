package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {

    Review get(Long id);

    List<Review> getAll();

    String add(ReviewDto reviewDto, HttpServletRequest request) throws IncorrectUserException;

    String update(ReviewDto reviewDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;

}
