package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.post.meal.Meal;
import cz.daiton.foodsquare.post.review.Review;
import cz.daiton.foodsquare.post.thread.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByMeal(Meal meal);

    boolean existsByReview(Review review);

    boolean existsByThread(Thread thread);

    Optional<Post> findByMeal(Meal meal);

    List<Post> findAllByOrderByCreatedAtDesc();

}
