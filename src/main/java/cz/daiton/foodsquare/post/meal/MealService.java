package cz.daiton.foodsquare.post.meal;

import java.util.List;

public interface MealService {

    Meal get(Long id);

    List<Meal> getAll();

    void add(Meal meal);

    void update(MealDto mealDto, Long id);

    void delete(Long id);

}
