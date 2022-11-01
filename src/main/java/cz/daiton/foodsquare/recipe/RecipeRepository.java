package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByOrderByUpdatedAtDesc();

    List<Recipe> findAllByAppUserOrderByUpdatedAtDesc(AppUser appUser);

    List<Recipe> findAllByFavoritesOrderByUpdatedAtDesc(AppUser appUser);

}
