package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByOrderByUpdatedAtDesc();

    List<Recipe> findAllByAppUserOrderByUpdatedAtDesc(AppUser appUser);

    List<Recipe> findAllByFavoritesOrderByUpdatedAtDesc(AppUser appUser);

    List<Recipe> findAllByCategoriesIn(Set<Category> categories);
}
