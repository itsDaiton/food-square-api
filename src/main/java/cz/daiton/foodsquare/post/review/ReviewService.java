package cz.daiton.foodsquare.post.review;

import cz.daiton.foodsquare.appuser.AppUser;

import java.util.List;

public interface ReviewService {

    Review get(Long id);

    List<Review> getAll();

    void add(ReviewDto reviewDto);

    void update(ReviewDto reviewDto, Long id);

    void delete(Long id);

    Review findTopByAppUserOrderByIdDesc(AppUser appUser);
}
