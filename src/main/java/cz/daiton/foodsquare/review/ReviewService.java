package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface ReviewService {

    Review get(Long id);

    List<Review> getAll();

    Review getByRecipe(Long id, HttpServletRequest request) throws IncorrectUserException;

    String add(ReviewDto reviewDto, HttpServletRequest request) throws IncorrectUserException;

    String update(ReviewDto reviewDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;

    String deleteByRecipe(Long id, HttpServletRequest request) throws IncorrectUserException;

    Integer countByRecipe(Long id);

    BigDecimal getAverageRating(Long id);

    Boolean containsReview(Long id, HttpServletRequest request) throws IncorrectUserException;

}
