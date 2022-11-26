package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.InsertResponse;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping(value = "api/v1/recipes")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints for CRUD operations with recipes.", name = "Recipe Controller")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Returns a specific recipe based on given parameter.")
    public Recipe getRecipe(@Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return recipeService.get(id);
    }

    @GetMapping()
    @Operation(summary = "Returns list of all recipes in the application.")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @GetMapping(value = "/{id}/categories")
    @Operation(summary = "Returns list of all categories in a specific recipe.")
    public Set<Category> getCategoriesInRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id) {
        return recipeService.getCategoriesInRecipe(id);
    }

    @GetMapping(value = "/my-feed")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Returns a personalized feed of recipes for a authenticated user.")
    public List<Recipe> getMyFeed(HttpServletRequest request) throws IncorrectUserException {
        return recipeService.getAllRecipesOfFollowingAndMine(request);
    }

    @GetMapping(value = "/user/{id}")
    @Operation(summary = "Returns list of all recipes of a specific user.")
    public List<Recipe> getAllByUser(
            @Parameter(description = "ID of the user.", example = "1") @PathVariable Long id) {
        return recipeService.getAllByUser(id);
    }

    @GetMapping(value = "/extended")
    @Operation(summary = "Returns list of all extended recipes.")
    public List<RecipeExtended> getAllRecipesExtended() {
        return recipeService.getAllExtendedRecipes();
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Adds a recipe.")
    public ResponseEntity<?> addRecipe(
            @Valid @RequestBody RecipeDto recipeDto,
            HttpServletRequest request) throws Exception {
        Recipe recipe = recipeService.add(recipeDto, request);
        return ResponseEntity
                .ok()
                .body(new InsertResponse(recipe.getId(), "Recipe has been successfully created."));
    }

    @PutMapping(value = "/{id}/image")
    @Operation(summary = "Adds a image to recipe.")
    public ResponseEntity<?> addImage(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            @Parameter(description = "Multipart image file.") @RequestParam("image") MultipartFile file,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.uploadImage(id, file, request)));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Edits a recipe.")
    public ResponseEntity<?> updateRecipe(
            @Valid @RequestBody RecipeDto recipeDto,
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.update(recipeDto, id, request)));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a recipe.")
    public ResponseEntity<?> deleteRecipe(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.delete(id, request)));
    }

    @GetMapping(value = "/{id}/check-favorite")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Checks whether user has added a specific recipe to favorites.")
    public Boolean checkForFavorite(
            @Parameter(description = "ID of the recipe.", example = "1") @PathVariable Long id,
            HttpServletRequest request) throws Exception {
        return recipeService.checkFavorite(id, request);
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    InvalidDataAccessApiUsageException.class,
                    RuntimeException.class,
                    InvalidDataAccessApiUsageException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Wrong format. Try again.";
        }
        else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "Wrong argument type. Please try again.";
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "Wrong request method. Please try again.";
        }
        else if (e instanceof MissingServletRequestPartException) {
            message = "Please choose an image.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
