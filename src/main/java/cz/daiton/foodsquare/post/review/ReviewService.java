package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.security.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {

    Review get(Long id);

    List<Review> getAll();

    Review add(ReviewDto reviewDto);

    String update(ReviewDto reviewDto, Long id, HttpServletRequest request) throws IncorrectUserException;

}
