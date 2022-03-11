package cz.daiton.foodsquare.ingredients_in_meal;

import java.util.List;

public interface IngredientsInMealService {

    IngredientsInMeal get(Long id);

    List<IngredientsInMeal> getAll();

    void add(IngredientsInMealDto ingredientsInMealDto);

    void update(IngredientsInMealDto ingredientsInMealDto, Long id);

    void delete(Long id);

}
