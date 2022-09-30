package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.category.CategoryInputDto;
import cz.daiton.foodsquare.category.CategoryRepository;
import cz.daiton.foodsquare.category.CategoryType;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Recipe get(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id'" + id + "' was not found.")
        );
    }

    @Override
    public List<Recipe> getAll() {
        return recipeRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public String add(RecipeDto recipeDto, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = new Recipe();
        AppUser appUser = appUserRepository.findById(recipeDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + recipeDto.getAppUser() + "' does not exist.")
        );
        CategoryInputDto inputs = recipeDto.getInputs();

        if (appUserService.checkUser(recipeDto.getAppUser(), request)) {
            handleInsert(recipeDto, recipe, inputs);
            recipe.setAppUser(appUser);
            recipeRepository.save(recipe);
            return "Recipe has been successfully created.";
        }
        return "There has been a error while trying to add the recipe.";
    }

    @Override
    public String update(RecipeDto recipeDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' has not been found.")
        );
        CategoryInputDto inputs = recipeDto.getInputs();

        if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
            handleInsert(recipeDto, recipe, inputs);
            recipeRepository.save(recipe);
            return "Recipe has been successfully updated.";
        }
        return "There has been a error while trying to update the recipe.";
    }

    private void handleInsert(RecipeDto recipeDto, Recipe recipe, CategoryInputDto inputs) {
        Set<Category> categories = handleInputs(inputs);

        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setTimeToPrepare(recipeDto.getTimeToPrepare());
        recipe.setTimeToCook(recipeDto.getTimeToCook());
        recipe.setUpdatedAt(LocalDateTime.now());
        recipe.setCategories(categories);

        String meal = recipeDto.getMeal();
        if (meal.equals("breakfast") || meal.equals("lunch") || meal.equals("dinner") || meal.equals("snack")) {
            recipe.setMeal(RecipeMeal.valueOf(meal.toUpperCase()));
        }
        else {
            throw new NoSuchElementException("Please enter a correct meal type.");
        }

    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' does not exist. You cannot delete it.")
        );

        if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
            recipeRepository.deleteById(id);
            return "Recipe has been successfully deleted.";
        }
        return "There has been a error while trying to delete the recipe.";
    }

    @Override
    public Set<Category> getCategoriesInRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' does not exist.")
        );
        return categoryRepository.findAllByRecipes(recipe);
    }

    private Set<Category> handleInputs(CategoryInputDto inputs) {
        Set<Category> categories = new HashSet<>();
        if (!inputs.getGluten()) {
            handleCategory(CategoryType.GLUTEN_FREE, categories);
        }
        if (!inputs.getCrustaceans() && !inputs.getMolluscs()) {
            handleCategory(CategoryType.SHELLFISH_FREE, categories);
        }
        if (!inputs.getEggs()) {
            handleCategory(CategoryType.EGG_FREE, categories);
        }
        if (!inputs.getFish()) {
            handleCategory(CategoryType.FISH_FREE, categories);
        }
        if (!inputs.getSesame()) {
            handleCategory(CategoryType.SESAME_FREE, categories);
        }
        if (!inputs.getNuts()) {
            handleCategory(CategoryType.NUT_FREE, categories);
        }
        if (!inputs.getSoy()) {
            handleCategory(CategoryType.SOY_FREE, categories);
        }
        if (!inputs.getMilk()) {
            handleCategory(CategoryType.LACTOSE_FREE, categories);
        }
        if (!inputs.getPeanut()) {
            handleCategory(CategoryType.PEANUT_FREE, categories);
        }
        if (!inputs.getCelery()) {
            handleCategory(CategoryType.CELERY_FREE, categories);
        }
        if (!inputs.getMustard()) {
            handleCategory(CategoryType.MUSTARD_FREE, categories);
        }
        if (!inputs.getSulphur()) {
            handleCategory(CategoryType.SULPHUR_FREE, categories);
        }
        if (!inputs.getLupin()) {
            handleCategory(CategoryType.LUPIN_FREE, categories);
        }
        if (!inputs.getMeat()) {
            handleCategory(CategoryType.PESCATARIAN, categories);
        }
        if (!inputs.getMeat() && !inputs.getFish() && !inputs.getMolluscs() && !inputs.getCrustaceans()) {
            handleCategory(CategoryType.VEGETARIAN, categories);
        }
        if (!inputs.getMeat() && !inputs.getFish() && !inputs.getMolluscs() && !inputs.getCrustaceans() && !inputs.getEggs() && !inputs.getMilk()) {
            handleCategory(CategoryType.VEGAN, categories);
        }
        return categories;
    }

    private void handleCategory(CategoryType categoryType, Set<Category> categories) {
        Category category = categoryRepository.findByName(categoryType).orElseThrow(
                () -> new NoSuchElementException("This category does not exist.")
        );
        categories.add(category);
    }
}
