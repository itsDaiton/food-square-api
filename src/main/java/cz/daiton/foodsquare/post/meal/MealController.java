package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> addMeal(@RequestBody MealDto mealDto) {
        mealService.add(mealDto);
        AppUser appUser = appUserService.get(mealDto.getAppUser());
        Meal meal = mealService.findTopByAppUserOrderByIdDesc(appUser);
        return ResponseEntity
                .ok()
                .body(new PostContentResponse(
                        meal.getId(),
                        appUser.getId(),
                        "Meal has been successfully added."
                ));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAnyRole()")
    public String updateMeal(@RequestBody MealDto mealDto, @PathVariable Long id) {
        mealService.update(mealDto, id);
        return "Meal has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAnyRole()")
    public String deleteMeal(@PathVariable Long id) {
        mealService.delete(id);
        return "Meal has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy


}
