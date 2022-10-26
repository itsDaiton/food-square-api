package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.recipe.Recipe;

import java.util.List;

public interface MealPlanService {

    List<Recipe> generateMealPlan(MealPlanRequestDto dto);
}
