package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.InsertResponse;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping(value = "api/v1/recipes")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping(value = "/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.get(id);
    }

    @GetMapping()
    public List<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @GetMapping(value = "/{id}/categories")
    public Set<Category> getCategoriesInRecipe(@PathVariable Long id) {
        return recipeService.getCategoriesInRecipe(id);
    }

    @GetMapping(value = "/my-feed")
    @PreAuthorize("isAuthenticated()")
    public List<Recipe> getMyFeed(HttpServletRequest request) throws IncorrectUserException {
        return recipeService.getAllRecipesOfFollowingAndMine(request);
    }

    @GetMapping(value = "/user/{id}")
    public List<Recipe> getAllByUser(@PathVariable Long id) {
        return recipeService.getAllByUser(id);
    }

    @GetMapping(value = "/extended")
    public List<RecipeExtended> getAllRecipesExtended() {
        return recipeService.getAllExtendedRecipes();
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody RecipeDto recipeDto, HttpServletRequest request) throws Exception {
        Recipe recipe = recipeService.add(recipeDto, request);
        return ResponseEntity
                .ok()
                .body(new InsertResponse(recipe.getId(), "Recipe has been successfully created."));
    }

    @PutMapping(value = "/{id}/image")
    public ResponseEntity<?> addImage(@PathVariable Long id, @RequestParam("image") MultipartFile file, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.uploadImage(id, file, request)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDto recipeDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.update(recipeDto, id, request)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.delete(id, request)));
    }

    @GetMapping(value = "/{id}/check-favorite")
    @PreAuthorize("isAuthenticated()")
    public Boolean checkForFavorite(@PathVariable Long id, HttpServletRequest request) throws Exception {
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
            message = "This is not valid ID. Please try again.";
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
