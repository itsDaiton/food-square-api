package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "api/v1/meals")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class MealController {

    private final MealService mealService;

    private final AppUserService appUserService;

    public MealController(MealService mealService, AppUserService appUserService) {
        this.mealService = mealService;
        this.appUserService = appUserService;
    }

    @GetMapping(value = "get/{id}")
    public Meal getMeal(@PathVariable Long id) {
        return mealService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Meal> getAllMeals() {
        return mealService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addMeal(@Valid @RequestBody MealDto mealDto, HttpServletRequest request) throws Exception {
        Meal meal = mealService.add(mealDto);
        return ResponseEntity
                .ok()
                .body(new PostContentResponse(
                        meal.getId(),
                        appUserService.getLocalUser(request).getId(),
                        "Meal has been successfully added."
                ));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateMeal(@Valid @RequestBody MealDto mealDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        mealService.update(mealDto, id, request);
        return ResponseEntity
                .ok()
                .body(new MessageResponse(mealService.update(mealDto, id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
