package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.category.CategoryRepository;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MealPlanServiceImpl implements MealPlanService {

    private final RecipeIngredientService recipeIngredientService;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Recipe> generateMealPlan(MealPlanRequestDto dto) {
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            throw new RuntimeException("Unable to create a meal plan. We did not find any recipes.");
        }
        List<Recipe> recipesFiltered = new ArrayList<>();
        Set<Category> categories = new HashSet<>();

        if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
            for (Category c : dto.getCategories()) {
                Category category = categoryRepository.findByName(c.getName()).orElseThrow(
                        () -> new NoSuchElementException("Category: '" + c.getName() + "' does not exist.")
                );
                categories.add(category);
            }
            for (Recipe r : recipes) {
                if (r.getCategories().containsAll(categories)) {
                    recipesFiltered.add(r);
                }
            }
        }
        else {
            recipesFiltered.addAll(recipes);
        }

        if (recipesFiltered.isEmpty()) {
            throw new RuntimeException("We could not build a meal plan based on those inputs.");
        }

        /*
        switch (dto.getAmount().intValue()) {
            case 1:
                //1
                break;
            case 2:
                //2
                break;
            case 3:
                //3
                break;
            case 4:
                //4
                break;
            case 5:
                //5
                break;
            default:
        }
        */
        return recipesFiltered;
    }
}
