package cz.daiton.foodsquare.IO;

import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.ingredient.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CustomWriter implements ItemWriter<Ingredient> {

    private final IngredientRepository ingredientRepository;

    @Override
    public void write(List<? extends Ingredient> list) throws Exception {
        for (Ingredient data : list) {
            if (!ingredientRepository.existsByCode(data.getCode())) {
                ingredientRepository.save(data);
            }
        }
    }
}
