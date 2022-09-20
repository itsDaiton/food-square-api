package cz.daiton.foodsquare.IO;

import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.ingredient.IngredientRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomWriter implements ItemWriter<Ingredient> {

    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public void write(List<? extends Ingredient> list) throws Exception {
        for (Ingredient data : list) {
            ingredientRepository.save(data);
        }
    }
}
