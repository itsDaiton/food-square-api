package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface RecipeService {

    Recipe get(Long id);

    List<Recipe> getAll();

    Set<Category> getCategoriesInRecipe(Long id);

    Recipe add(RecipeDto recipeDto, HttpServletRequest request) throws IncorrectUserException;

    String update(RecipeDto recipeDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;
}
