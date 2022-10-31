package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe_ingredient.NutritionAnalysis;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MealPlanOutput {

    List<Recipe> recipes;

    NutritionAnalysis analysis;
}
