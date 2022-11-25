package cz.daiton.foodsquare.recipe_ingredient;

import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "recipe_ingredient")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Class representing a recipe-ingredient relation.")
public class RecipeIngredient {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Schema(description = "Unique identifier for recipe-ingredient relation.", example = "1")
    private Long id;

    @NotNull(message = required)
    @Min(value = 1, message = "You have to include at least 1g of the ingredient.")
    @Schema(description = "Amount of the ingredient in recipe (in grams).", example = "100")
    private Integer amount;

    @ManyToOne()
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
    private Recipe recipe;

    @ManyToOne()
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id", nullable = false)
    private Ingredient ingredient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
