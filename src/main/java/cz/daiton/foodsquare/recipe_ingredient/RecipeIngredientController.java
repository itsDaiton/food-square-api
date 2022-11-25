package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/recipe-ingredients")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(
        description = "Set of endpoints for CRUD operations with recipe-ingredient relations.",
        name = "Recipe Ingredient Controller"
)
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Returns a specific recipe-ingredient relation based on given parameter.")
    public RecipeIngredient getRecipeIngredient(
            @Parameter(description = "ID of the recipe-ingredient relation.", example = "1") @PathVariable Long id) {
        return recipeIngredientService.get(id);
    }

    @GetMapping()
    @Operation(summary = "Returns a list of all recipe-ingredient relations.")
    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientService.getAll();
    }

    @GetMapping(value = "/recipe/{id}")
    @Operation(summary = "Returns all recipe-ingredient relations for a recipe.")
    public List<RecipeIngredient> getAllByRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return recipeIngredientService.getAllByRecipe(id);
    }

    @GetMapping(value = "/recipe/{id}/nutrition-analysis")
    @Operation(summary = "Returns a nutrition analysis for a recipe.")
    public NutritionAnalysis getNutritionAnalysis(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return recipeIngredientService.calculateNutritionAnalysisForRecipe(id);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Adds a ingredient to recipe.")
    public ResponseEntity<?> addIngredientToRecipe(
            @Valid @RequestBody RecipeIngredientDto recipeIngredientDto,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.add(recipeIngredientDto, request)));
    }

    @PostMapping(value = "/addAll")
    @Operation(summary = "Adds multiple ingredients to recipe.")
    public ResponseEntity<?> addAllIngredientsToRecipe(
            @Valid @RequestBody RecipeIngredientListDto list,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.addAll(list, request)));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Updates amount of ingredient in recipe.")
    public ResponseEntity<?> updateIngredientAmount(
            @Valid @RequestBody RecipeIngredientDto recipeIngredientDto,
            @Parameter(description = "ID of the recipe-ingredient relation.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.update(recipeIngredientDto, id, request)));
    }

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Deletes ingredient from recipe.")
    public ResponseEntity<?> deleteIngredientFromRecipe(
            @Parameter(description = "ID of the recipe-ingredient relation.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.delete(id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    InvalidDataAccessApiUsageException.class,
                    DataIntegrityViolationException.class,
                    NullPointerException.class,
                    HttpRequestMethodNotSupportedException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Wrong format. Try again.";
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "Wrong request method. Please try again.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
