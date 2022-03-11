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
        return ingredientRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public void add(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    @Override
    public void update(IngredientDto ingredientDto, Long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(NoSuchElementException::new);
        ingredient.setName(ingredientDto.getName());
        ingredient.setCategory(ingredientDto.getCategory());
        ingredientRepository.save(ingredient);
    }

    @Override
    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }
}
