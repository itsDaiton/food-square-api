package cz.daiton.foodsquare.ingredient;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/ingredients")
@CrossOrigin
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping(value = "get/{id}")
    public Ingredient getIngredient(@PathVariable Long id) {
        return ingredientService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public String addIngredient(@RequestBody Ingredient ingredient) {
        ingredientService.add(ingredient);
        return "Ingredient has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateIngredient(@RequestBody IngredientDto ingredientDto, @PathVariable Long id) {
        ingredientService.update(ingredientDto, id);
        return "Ingredient has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteIngredient(@PathVariable Long id) {
        ingredientService.delete(id);
        return "Ingredient has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy

}
