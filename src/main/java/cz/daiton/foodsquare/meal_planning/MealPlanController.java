package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/meal-planning")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @PutMapping(value = "/generate")
    public MealPlanOutput generateMealPlan(@Valid @RequestBody MealPlanRequestDto dto) throws Exception {
        return mealPlanService.generateMealPlan(dto);
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
            message = "Wrong argument type. Please try again.";
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
