package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecipeService {

    Recipe get(Long id);

    List<Recipe> getAll();

    String add(RecipeDto recipeDto, HttpServletRequest request) throws IncorrectUserException;

    String update(RecipeDto recipeDto, Long id, HttpServletRequest request) throws IncorrectUserException;

    String delete(Long id, HttpServletRequest request) throws IncorrectUserException;
}
