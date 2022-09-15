package cz.daiton.foodsquare.ingredients_in_meal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/meal-ingredients")
@CrossOrigin
public class IngredientsInMealController {

    private final IngredientsInMealService ingredientsInMealService;

    public IngredientsInMealController(IngredientsInMealService ingredientsInMealService) {
        this.ingredientsInMealService = ingredientsInMealService;
    }

    @GetMapping(value = "get/{id}")
    public IngredientsInMeal getIngredientsInMeal(@PathVariable Long id) {
        return ingredientsInMealService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<IngredientsInMeal> getAllIngredientsInMeal() {
        return ingredientsInMealService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public String addIngredientToMeal(@RequestBody IngredientsInMealDto ingredientsInMealDto) {
        ingredientsInMealService.add(ingredientsInMealDto);
        return "Ingredient has been successfully added to meal.";
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateIngredientInMeal(@RequestBody IngredientsInMealDto ingredientsInMealDto, @PathVariable Long id) {
        ingredientsInMealService.update(ingredientsInMealDto, id);
        return "Ingredient has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteIngredientInMeal(@PathVariable Long id) {
        ingredientsInMealService.delete(id);
        return "Ingredient has been successfully deleted from the meal.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy
}
