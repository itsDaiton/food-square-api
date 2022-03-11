package cz.daiton.foodsquare.post.review;

import java.util.List;

public interface ReviewService {

    Review get(Long id);

    List<Review> getAll();

    void add(Review review);

    void update(ReviewDto reviewDto, Long id);

    void delete(Long id);
}
