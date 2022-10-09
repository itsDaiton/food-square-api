package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByUpdatedAtDesc();

    List<Review> findAllByLikes(AppUser appUser);

    List<Review> findAllByRecipe(Recipe recipe);

    Integer countAllByRecipe(Recipe recipe);

    Boolean existsByAppUserAndRecipe(AppUser appUser, Recipe recipe);

    Optional<Review> findByAppUserAndRecipe(AppUser appUser, Recipe recipe);

}
