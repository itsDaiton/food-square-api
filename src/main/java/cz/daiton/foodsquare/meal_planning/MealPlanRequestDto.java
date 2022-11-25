package cz.daiton.foodsquare.meal_planning;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Object representing request to generate meal plan.")
public class MealPlanRequestDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @Schema(description = "Allowed categories.")
    Set<String> categories;

    @NotNull(message = required)
    @Min(value = 1, message = "You have to choose at least 1 recipe.")
    @Schema(description = "Amount of meals generated.", example = "3")
    private Long amount;

    @NotNull(message = required)
    @DecimalMin(value = "100.00", message = "You have to choose at least 100 calories.")
    @Schema(description = "Amount of calories.", example = "2000")
    private BigDecimal calories;
}
