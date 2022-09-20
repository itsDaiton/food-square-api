package cz.daiton.foodsquare.ingredient;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<?> handleExceptions(NoSuchElementException e) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(e.getMessage()));
    }
}
