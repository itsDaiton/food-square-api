package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe_ingredient.NutritionAnalysis;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Object representing output of meal planning.")
public class MealPlanOutput {

    @Schema(description = "List of generated recipes.")
    List<Recipe> recipes;

    @Schema(description = "Nutrition analysis for all recipes.")
    NutritionAnalysis analysis;
}
