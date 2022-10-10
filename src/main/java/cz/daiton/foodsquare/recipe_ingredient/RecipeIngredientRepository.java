package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    boolean existsByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

    List<RecipeIngredient> findAllByRecipe(Recipe recipe);

    void deleteAllByRecipe(Recipe recipe);

}
