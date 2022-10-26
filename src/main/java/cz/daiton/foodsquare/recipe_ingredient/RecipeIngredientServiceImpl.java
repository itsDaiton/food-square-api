package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.ingredient.IngredientRepository;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public RecipeIngredient get(Long id) {
        return recipeIngredientRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no record with id: '" + id + "' present in the database.")
        );
    }

    @Override
    public List<RecipeIngredient> getAll() {
        return recipeIngredientRepository.findAll();
    }


    @Override
    public List<RecipeIngredient> getAllByRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        return recipeIngredientRepository.findAllByRecipe(recipe);
    }

    @Override
    public NutritionAnalysis calculateNutritionAnalysisForRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        return getNutritionAnalysis(recipe);
    }

    @Override
    public String add(RecipeIngredientDto recipeIngredientDto, HttpServletRequest request) throws IncorrectUserException {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Recipe recipe = recipeRepository.findById(recipeIngredientDto.getRecipe()).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + recipeIngredientDto.getRecipe() + "' was not found.")
        );
        Ingredient ingredient = ingredientRepository.findById(recipeIngredientDto.getIngredient()).orElseThrow(
                () -> new NoSuchElementException("Ingredient with id: '" + recipeIngredientDto.getIngredient() + "' was not found.")
        );
        AppUser appUser = appUserRepository.findById(recipe.getAppUser().getId()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + recipe.getAppUser().getId() + "' does not exist.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {
            if (recipeIngredientRepository.existsByRecipeAndIngredient(recipe, ingredient)) {
                throw new DataIntegrityViolationException("Recipe with id: '" + recipe.getId() + "' already contains ingredient with id: '" + ingredient.getId() + "'.");
            }
            recipeIngredient.setAmount(recipeIngredientDto.getAmount());
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setRecipe(recipe);
            recipeIngredientRepository.save(recipeIngredient);
            return "Ingredient has been successfully added to recipe.";
        }
        return "There has been a error while trying to add the ingredient to recipe.";
    }

    @Override
    public String addAll(RecipeIngredientListDto listDto, HttpServletRequest request) throws IncorrectUserException {
        List<RecipeIngredientDto> recipeIngredientDtoList = listDto.getRecipeIngredients();
        if (recipeIngredientDtoList.isEmpty()) {
            throw new NullPointerException("List of ingredients cannot be empty.");
        }
        List<RecipeIngredient> ingredients = new ArrayList<>();

        for (RecipeIngredientDto ingredientDto : recipeIngredientDtoList) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            Recipe recipe = recipeRepository.findById(ingredientDto.getRecipe()).orElseThrow(
                    () -> new NoSuchElementException("Recipe with id: '" + ingredientDto.getRecipe() + "' was not found.")
            );
            Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredient()).orElseThrow(
                    () -> new NoSuchElementException("Ingredient with id: '" + ingredientDto.getIngredient() + "' was not found.")
            );
            AppUser appUser = appUserRepository.findById(recipe.getAppUser().getId()).orElseThrow(
                    () -> new NoSuchElementException("User with id: '" + recipe.getAppUser().getId() + "' does not exist.")
            );

            if (appUserService.checkUser(appUser.getId(), request)) {
                if (recipeIngredientRepository.existsByRecipeAndIngredient(recipe, ingredient)) {
                    throw new DataIntegrityViolationException("Recipe with id: '" + recipe.getId() + "' already contains ingredient with id: '" + ingredient.getId() + "'.");
                }
                recipeIngredient.setAmount(ingredientDto.getAmount());
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setRecipe(recipe);
                ingredients.add(recipeIngredient);
            }
        }
        recipeIngredientRepository.saveAll(ingredients);
        return "All ingredients have been successfully added to recipe.";
    }

    @Override
    public String update(RecipeIngredientDto recipeIngredientDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no record with id: '" + id + "' present in the database.")
        );
        Recipe recipe = recipeRepository.findById(recipeIngredient.getRecipe().getId()).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + recipeIngredient.getRecipe().getId() + "' was not found.")
        );
        AppUser appUser = appUserRepository.findById(recipe.getAppUser().getId()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + recipe.getAppUser().getId() + "' does not exist.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {
            recipeIngredient.setAmount(recipeIngredientDto.getAmount());
            recipeIngredientRepository.save(recipeIngredient);
            return "Amount of ingredients has been successfully updated.";
        }
        return "There has been a error while trying to update the amount of ingredients.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no record with id: '" + id + "' present in the database.")
        );
        Recipe recipe = recipeIngredient.getRecipe();
        AppUser appUser = recipe.getAppUser();

        if (appUserService.checkUser(appUser.getId(), request)) {
            recipeIngredientRepository.deleteById(id);
            return "Ingredient has been successfully deleted from recipe.";
        }
        return "There has been a error while trying to delete the ingredient from recipe.";
    }

    public NutritionAnalysis getNutritionAnalysis(Recipe recipe) {
        List<RecipeIngredient> ingredientList = recipeIngredientRepository.findAllByRecipe(recipe);

        double totalCalories = 0;
        double totalFat = 0;
        double totalSaturatedFattyAcids = 0;
        double totalMonounsaturatedFattyAcids = 0;
        double totalPolyunsaturatedFattyAcids = 0;
        double totalTransFattyAcids = 0;
        double totalCarbohydrateTotal = 0;
        double totalCarbohydrateAvailable = 0;
        double totalFibre = 0;
        double totalSugar = 0;
        double totalProtein = 0;
        double totalSodium = 0;
        double totalSalt = 0;
        double totalWater = 0;

        if (!ingredientList.isEmpty()) {
            for (RecipeIngredient r : ingredientList) {
                totalCalories += (r.getAmount() * r.getIngredient().getCalories().doubleValue());
                totalFat += (r.getAmount() * r.getIngredient().getFat().doubleValue());
                totalSaturatedFattyAcids += (r.getAmount() * r.getIngredient().getSaturatedFattyAcids().doubleValue());
                totalMonounsaturatedFattyAcids += (r.getAmount() * r.getIngredient().getMonounsaturatedFattyAcids().doubleValue());
                totalPolyunsaturatedFattyAcids += (r.getAmount() * r.getIngredient().getPolyunsaturatedFattyAcids().doubleValue());
                totalTransFattyAcids += (r.getAmount() * r.getIngredient().getTransFattyAcids().doubleValue());
                totalCarbohydrateTotal += (r.getAmount() * r.getIngredient().getCarbohydrateTotal().doubleValue());
                totalCarbohydrateAvailable += (r.getAmount() * r.getIngredient().getCarbohydrateAvailable().doubleValue());
                totalFibre += (r.getAmount() * r.getIngredient().getFibre().doubleValue());
                totalSugar += (r.getAmount() * r.getIngredient().getSugar().doubleValue());
                totalProtein += (r.getAmount() * r.getIngredient().getProtein().doubleValue());
                totalSodium += (r.getAmount() * r.getIngredient().getSodium().doubleValue());
                totalSalt += (r.getAmount() * r.getIngredient().getSalt().doubleValue());
                totalWater += (r.getAmount() * r.getIngredient().getWater().doubleValue());
            }
        }

        return new NutritionAnalysis(
                BigDecimal.valueOf(totalCalories).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalFat).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSaturatedFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalMonounsaturatedFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalPolyunsaturatedFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalTransFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalCarbohydrateTotal).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalCarbohydrateAvailable).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalFibre).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSugar).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalProtein).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSodium).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSalt).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalWater).setScale(1, RoundingMode.HALF_EVEN)
        );
    }
}
