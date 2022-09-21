package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    boolean existsByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

}
