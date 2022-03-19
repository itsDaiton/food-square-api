package cz.daiton.foodsquare.ingredients_in_meal;

import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.ingredient.IngredientRepository;
import cz.daiton.foodsquare.post.meal.Meal;
import cz.daiton.foodsquare.post.meal.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class IngredientsInMealServiceImpl implements IngredientsInMealService {

    private final IngredientsInMealRepository ingredientsInMealRepository;
    private final IngredientRepository ingredientRepository;
    private final MealRepository mealRepository;

    public IngredientsInMealServiceImpl(IngredientsInMealRepository ingredientsInMealRepository, IngredientRepository ingredientRepository, MealRepository mealRepository) {
        this.ingredientsInMealRepository = ingredientsInMealRepository;
        this.ingredientRepository = ingredientRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    public IngredientsInMeal get(Long id) {
        return ingredientsInMealRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<IngredientsInMeal> getAll() {
        return ingredientsInMealRepository.findAll();
    }

    @Override
    public void add(IngredientsInMealDto ingredientsInMealDto) {
        Ingredient ingredient = ingredientRepository.findById(ingredientsInMealDto.getIngredient()).orElseThrow(NoSuchElementException::new);
        Meal meal = mealRepository.findById(ingredientsInMealDto.getMeal()).orElseThrow(NoSuchElementException::new);
        IngredientsInMeal ingredientsInMeal = new IngredientsInMeal();

        ingredientsInMeal.setAmount(ingredientsInMealDto.getAmount());
        ingredientsInMeal.setIngredient(ingredient);
        ingredientsInMeal.setMeal(meal);

        ingredientsInMealRepository.save(ingredientsInMeal);
    }

    @Override
    public void update(IngredientsInMealDto ingredientsInMealDto, Long id) {
        IngredientsInMeal ingredientsInMeal = ingredientsInMealRepository.findById(id).orElseThrow(NoSuchElementException::new);
        Ingredient ingredient = ingredientRepository.findById(ingredientsInMealDto.getIngredient()).orElseThrow(NoSuchElementException::new);
        Meal meal = mealRepository.findById(ingredientsInMealDto.getMeal()).orElseThrow(NoSuchElementException::new);

        ingredientsInMeal.setAmount(ingredientsInMealDto.getAmount());
        ingredientsInMeal.setIngredient(ingredient);
        ingredientsInMeal.setMeal(meal);

        ingredientsInMealRepository.save(ingredientsInMeal);
    }

    @Override
    public void delete(Long id) {
        ingredientsInMealRepository.deleteById(id);
    }
}
