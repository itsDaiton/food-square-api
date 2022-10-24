package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.recipe.Recipe;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/recipe-ingredients")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @GetMapping(value = "get/{id}")
    public RecipeIngredient getRecipeIngredient(@PathVariable Long id) {
        return recipeIngredientService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientService.getAll();
    }

    @GetMapping(value = "/getByRecipe/{id}")
    public List<RecipeIngredient> getAllByRecipe(@PathVariable Long id) {
        return recipeIngredientService.getAllByRecipe(id);
    }

    @GetMapping(value = "/calculateNutritionAnalysis/{id}")
    public NutritionAnalysis getNutritionAnalysis(@PathVariable Long id) {
        return recipeIngredientService.calculateNutritionAnalysisForRecipe(id);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addIngredientToRecipe(@Valid @RequestBody RecipeIngredientDto recipeIngredientDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.add(recipeIngredientDto, request)));
    }

    @PostMapping(value = "/addAll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addAllIngredientsToRecipe(@Valid @RequestBody RecipeIngredientListDto list, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.addAll(list, request)));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateIngredientAmount(@Valid @RequestBody RecipeIngredientDto recipeIngredientDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeIngredientService.update(recipeIngredientDto, id, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteIngredientFromRecipe(@PathVariable Long id, HttpServletRequest request) throws Exception {
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
                    NullPointerException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Wrong format. Try again.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
