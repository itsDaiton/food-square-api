package cz.daiton.foodsquare.recipe_ingredient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Object representing a recipe-ingredient relation.")
public class RecipeIngredientDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotNull(message = required)
    @Min(value = 1, message = "You have to include at least 1g of the ingredient.")
    @Schema(description = "Amount of the ingredient in recipe (in grams).", example = "100")
    private Integer amount;

    @Schema(description = "ID of the recipe.", example = "1")
    private Long recipe;

    @Schema(description = "ID of the ingredient.", example = "1")
    private Long ingredient;
}
