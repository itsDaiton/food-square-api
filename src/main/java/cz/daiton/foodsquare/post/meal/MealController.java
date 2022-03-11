package cz.daiton.foodsquare.post.meal;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/meals")
@CrossOrigin
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
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
    public String addMeal(@RequestBody Meal meal) {
        mealService.add(meal);
        return "Meal has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updateMeal(@RequestBody MealDto mealDto, @PathVariable Long id) {
        mealService.update(mealDto, id);
        return "Meal has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteMeal(@PathVariable Long id) {
        mealService.delete(id);
        return "Meal has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy


}
