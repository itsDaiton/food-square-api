package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.security.IncorrectUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
public interface MealService {

    Meal get(Long id);

    List<Meal> getAll();

    Meal add(MealDto mealDto);

    String update(MealDto mealDto, Long id, HttpServletRequest request) throws IncorrectUserException;

}
