package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.category.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class MealPlanRequestDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    Set<Category> categories;

    @NotNull(message = required)
    @Min(value = 1, message = "You have to choose at least 1 recipe.")
    private Long amount;

    // Main inputs
    private BigDecimal calories;

    private BigDecimal fat;

    private BigDecimal protein;

    private BigDecimal carbohydrateTotal;


    // Secondary inputs
    private BigDecimal saturatedFattyAcids;

    private BigDecimal monounsaturatedFattyAcids;

    private BigDecimal polyunsaturatedFattyAcids;

    private BigDecimal transFattyAcids;

    private BigDecimal carbohydrateAvailable;

    private BigDecimal fibre;

    private BigDecimal sugar;

    private BigDecimal sodium;

    private BigDecimal salt;

    private BigDecimal water;
}
