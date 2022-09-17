package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import cz.daiton.foodsquare.security.IncorrectUserException;
import cz.daiton.foodsquare.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/meals")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class MealController {

    private final MealService mealService;

    private final AppUserService appUserService;

    private final JwtUtils jwtUtils;

    public MealController(MealService mealService, AppUserService appUserService, JwtUtils jwtUtils) {
        this.mealService = mealService;
        this.appUserService = appUserService;
        this.jwtUtils = jwtUtils;
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
    public ResponseEntity<?> addMeal(@RequestBody MealDto mealDto, HttpServletRequest request) throws IncorrectUserException {
        Meal meal = mealService.add(mealDto);

        String jwt = jwtUtils.getJwtFromCookies(request);

        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            AppUser appUser = appUserService.findByUsername(username);
            return ResponseEntity
                    .ok()
                    .body(new PostContentResponse(
                            meal.getId(),
                            appUser.getId(),
                            "Meal has been successfully added."
                    ));
        }
        else {
            throw new IncorrectUserException("You cannot do this. You are not the same user.");
        }

    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateMeal(@RequestBody MealDto mealDto, @PathVariable Long id) {
        mealService.update(mealDto, id);
        return "Meal has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteMeal(@PathVariable Long id) {
        mealService.delete(id);
        return "Meal has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy


}
