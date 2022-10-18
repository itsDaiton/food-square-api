package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RecipeService {

    Recipe get(Long id);

    List<Recipe> getAll();

    List<Recipe> getAllRecipesOfFollowingAndMine(HttpServletRequest request) throws IncorrectUserException;

    Set<Category> getCategoriesInRecipe(Long id);

    Recipe add(RecipeDto recipeDto, HttpServletRequest request) throws IncorrectUserException, IOException;

    String update(RecipeDto recipeDto, Long id, HttpServletRequest request) throws IncorrectUserException, IOException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;
}
