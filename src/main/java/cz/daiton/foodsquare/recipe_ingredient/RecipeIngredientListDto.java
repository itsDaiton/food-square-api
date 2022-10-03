package cz.daiton.foodsquare.recipe_ingredient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class RecipeIngredientListDto {

    @Valid
    @NotEmpty(message = "List of ingredients cannot be empty.")
    private List<RecipeIngredientDto> recipeIngredients;
}
