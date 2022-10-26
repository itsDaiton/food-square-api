package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.category.CategoryRepository;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.recipe.Recipe;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping(value = "api/v1/meal-planning")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;
    private final CategoryRepository categoryRepository;

    @GetMapping(value = "/generate")
    public List<Recipe> generateMealPlan(@Valid @RequestBody MealPlanRequestDto dto) throws Exception {
        return mealPlanService.generateMealPlan(dto);
        //return categoryRepository.findAll();
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    InvalidDataAccessApiUsageException.class,
                    RuntimeException.class
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
