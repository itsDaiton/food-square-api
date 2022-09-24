package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @GetMapping(value = "get/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @GetMapping(value = "/getCategories/{id}")
    public Set<Category> getCategoriesInRecipe(@PathVariable Long id) {
        return recipeService.getCategoriesInRecipe(id);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody RecipeDto recipeDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.add(recipeDto, request)));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDto recipeDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.update(recipeDto, id, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.delete(id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    InvalidDataAccessApiUsageException.class,
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
