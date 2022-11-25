package cz.daiton.foodsquare.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "ingredient")
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Class representing a ingredient used in recipes.")
public class Ingredient {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Schema(description = "Unique identifier for ingredient.", example = "1")
    private Long id;

    @Column(
            unique = true,
            nullable = false
    )
    @Schema(description = "Unique pre-defined code.", example = "123")
    private Long code;

    @Column(
            nullable = false
    )
    @Schema(description = "Name of the ingredient.", example = "Garlic")
    private String name;

    @Schema(description = "Calories value of the ingredient.", example = "50")
    private BigDecimal calories;


    @Schema(description = "Fat value of the ingredient.", example = "10")
    private BigDecimal fat;

    @Schema(description = "Saturated fatty acids value of the ingredient.", example = "2")
    private BigDecimal saturatedFattyAcids;

    @Schema(description = "Monounsaturated fatty acids value of the ingredient.", example = "1.5")
    private BigDecimal monounsaturatedFattyAcids;

    @Schema(description = "Polyunsaturated fatty acids value of the ingredient.", example = "7.5")
    private BigDecimal polyunsaturatedFattyAcids;

    @Schema(description = "Trans-fatty acids value of the ingredient.", example = "16.6")
    private BigDecimal transFattyAcids;


    @Schema(description = "Carbohydrate total value of the ingredient.", example = "3")
    private BigDecimal carbohydrateTotal;

    @Schema(description = "Carbohydrate available value of the ingredient.", example = "2")
    private BigDecimal carbohydrateAvailable;

    @Schema(description = "Fibre value of the ingredient.", example = "100")
    private BigDecimal fibre;

    @Schema(description = "Sugar value of the ingredient.", example = "9")
    private BigDecimal sugar;


    @Schema(description = "Protein value of the ingredient.", example = "7.7")
    private BigDecimal protein;

    @Schema(description = "Sodium value of the ingredient.", example = "10")
    private BigDecimal sodium;

    @Schema(description = "Salt value of the ingredient.", example = "0")
    private BigDecimal salt;


    @Schema(description = "Water value of the ingredient.", example = "1.1")
    private BigDecimal water;

    @OneToMany(
            mappedBy = "ingredient"
    )
    @JsonIgnore
    private Set<RecipeIngredient> ingredientSet = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}