package cz.daiton.foodsquare.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredient;
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
@ToString
public class Ingredient {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            unique = true,
            nullable = false
    )
    private Long code;

    @Column(
            nullable = false
    )
    private String name;

    private BigDecimal calories;


    private BigDecimal fat;

    private BigDecimal saturatedFattyAcids;

    private BigDecimal monounsaturatedFattyAcids;

    private BigDecimal polyunsaturatedFattyAcids;

    private BigDecimal transFattyAcids;


    private BigDecimal carbohydrateTotal;

    private BigDecimal carbohydrateAvailable;

    private BigDecimal fibre;

    private BigDecimal sugar;


    private BigDecimal protein;

    private BigDecimal sodium;

    private BigDecimal salt;


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