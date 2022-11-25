package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Object representing a extended recipe.")
public class RecipeExtended {

    @Schema(description = "ID of the recipe.", example = "1")
    private Long id;

    @Schema(description = "Name of the recipe.", example = "Example recipe")
    private String name;

    @Schema(description = "Description of the recipe.", example = "Example description")
    private String description;

    @Schema(description = "Instructions of the recipe.", example = "Example instructions")
    private String instructions;

    @Schema(description = "Preparation time of the recipe.", example = "20")
    private Integer timeToPrepare;

    @Schema(description = "Cooking time of the recipe.", example = "10")
    private Integer timeToCook;

    @Schema(description = "Path to recipe image.", example = "/img/recipes/1.jpg")
    private String pathToImage;

    @Schema(description = "Date of last update to the recipe.")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the user.", example = "1")
    private AppUser appUser;

    private Set<Category> categories;

    @Schema(description = "Path to recipe image.", example = "SNACK")
    private RecipeMeal meal;

    @Schema(description = "Average review rating of the recipe.", example = "4.5")
    private BigDecimal avgRating;
}
