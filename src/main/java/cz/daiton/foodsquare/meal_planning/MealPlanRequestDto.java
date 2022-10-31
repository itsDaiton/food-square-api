package cz.daiton.foodsquare.meal_planning;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
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

    Set<String> categories;

    @NotNull(message = required)
    @Min(value = 1, message = "You have to choose at least 1 recipe.")
    private Long amount;

    @NotNull(message = required)
    @DecimalMin(value = "100.00", message = "Minimal value of calories is 100.")
    private BigDecimal calories;
}
