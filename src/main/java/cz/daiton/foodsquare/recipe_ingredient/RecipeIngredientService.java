package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecipeIngredientService {

    RecipeIngredient get(Long id);

    List<RecipeIngredient> getAll();

    List<RecipeIngredient> getAllByRecipe(Long id);

    NutritionAnalysis calculateNutritionAnalysisForRecipe(Long id);

    String add(RecipeIngredientDto recipeIngredientDto, HttpServletRequest request) throws IncorrectUserException;

    String addAll(RecipeIngredientListDto listDto, HttpServletRequest request) throws IncorrectUserException;

    String update(RecipeIngredientDto recipeIngredientDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;
}
