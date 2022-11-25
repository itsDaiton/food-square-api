package cz.daiton.foodsquare.recipe_ingredient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Schema(description = "Object representing a multiple recipe-ingredient relations.")
public class RecipeIngredientListDto {

    @Valid
    @NotEmpty(message = "List of ingredients cannot be empty.")
    private List<RecipeIngredientDto> recipeIngredients;
}
