package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.category.CategoryInputDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Object representing a recipe.")
public class RecipeDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotEmpty(message = required)
    @Schema(description = "Name of the recipe.", example = "Example recipe")
    private String name;

    @NotEmpty(message = required)
    @Schema(description = "Description of the recipe.", example = "Example description")
    private String description;

    @NotEmpty(message = required)
    @Schema(description = "Instructions of the recipe.", example = "Example instructions")
    private String instructions;

    @NotNull(message = required)
    @Min(value = 1, message = "Preparation time must be at least 1 minute.")
    @Schema(description = "Preparation time of the recipe.", example = "20")
    private Integer timeToPrepare;

    @NotNull(message = required)
    @Min(value = 1, message = "Time to cook must be at least 1 minute.")
    @Schema(description = "Cooking time of the recipe.", example = "10")
    private Integer timeToCook;

    @Schema(description = "Image of the recipe.")
    private MultipartFile image;

    @Schema(description = "ID of the user.", example = "1")
    private Long appUser;

    @NotEmpty(message = required)
    @Schema(description = "Meal type of the recipe.", example = "BREAKFAST")
    private String meal;

    @Valid
    @NotNull(message = required)
    private CategoryInputDto inputs;

}
