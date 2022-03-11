package cz.daiton.foodsquare.ingredient;

import java.util.List;

public interface IngredientService {

    Ingredient get(Long id);

    List<Ingredient> getAll();

    void add(Ingredient ingredient);

    void update(IngredientDto ingredientDto, Long id);

    void delete(Long id);
}
