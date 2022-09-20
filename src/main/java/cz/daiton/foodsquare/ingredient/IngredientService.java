package cz.daiton.foodsquare.ingredient;

import java.util.List;

public interface IngredientService {

    Ingredient get(Long id);

    List<Ingredient> getAll();
}
