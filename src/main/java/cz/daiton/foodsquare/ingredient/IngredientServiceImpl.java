package cz.daiton.foodsquare.ingredient;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient get(Long id) {
        return ingredientRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Ingredient with id: '" + id + "' was not found.")
        );
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }
}
