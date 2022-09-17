package cz.daiton.foodsquare.post.review;

import java.util.List;

public interface ReviewService {

    Review get(Long id);

    List<Review> getAll();

    Review add(ReviewDto reviewDto);

    void update(ReviewDto reviewDto, Long id);

    void delete(Long id);

    Review findTopByOrderByIdDesc();
}
