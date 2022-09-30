package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.CategoryInputDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RecipeDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotEmpty(message = required)
    private String name;

    @NotEmpty(message = required)
    private String description;

    @NotEmpty(message = required)
    private String instructions;

    @NotNull(message = required)
    @Min(value = 1, message = "Preparation time must be at least 1 minute.")
    private Integer timeToPrepare;

    @NotNull(message = required)
    @Min(value = 1, message = "Time to cook must be at least 1 minute.")
    private Integer timeToCook;

    private Long appUser;

    @NotNull(message = required)
    private String meal;

    @Valid
    @NotNull(message = required)
    private CategoryInputDto inputs;

}
