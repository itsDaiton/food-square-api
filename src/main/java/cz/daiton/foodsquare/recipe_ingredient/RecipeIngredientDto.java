package cz.daiton.foodsquare.recipe_ingredient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RecipeIngredientDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotNull(message = required)
    @Min(value = 1, message = "You have to include at least 1g of the ingredient.")
    private Integer amount;

    private Long recipe;

    private Long ingredient;
}
