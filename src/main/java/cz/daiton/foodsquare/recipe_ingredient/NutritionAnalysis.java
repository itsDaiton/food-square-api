package cz.daiton.foodsquare.recipe_ingredient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Object representing nutrition analysis for a recipe.")
public class NutritionAnalysis {

    @Schema(description = "Total calories of the recipe.", example = "5")
    private BigDecimal totalCalories;

    @Schema(description = "Total fat of the recipe.", example = "10")
    private BigDecimal totalFat;

    @Schema(description = "Total saturated fatty acids of the recipe.", example = "100")
    private BigDecimal totalSaturatedFattyAcids;

    @Schema(description = "Total monounsaturated fatty acids of the recipe.", example = "150")
    private BigDecimal totalMonounsaturatedFattyAcids;

    @Schema(description = "Total polyunsaturated fatty acids of the recipe.", example = "50")
    private BigDecimal totalPolyunsaturatedFattyAcids;

    @Schema(description = "Total trans-fatty acids of the recipe.", example = "123")
    private BigDecimal totalTransFattyAcids;

    @Schema(description = "Total carbohydrate (total) of the recipe.", example = "15")
    private BigDecimal totalCarbohydrateTotal;

    @Schema(description = "Total carbohydrate (available) of the recipe.", example = "25")
    private BigDecimal totalCarbohydrateAvailable;

    @Schema(description = "Total fibre of the recipe.", example = "80")
    private BigDecimal totalFibre;

    @Schema(description = "Total sugar of the recipe.", example = "25")
    private BigDecimal totalSugar;

    @Schema(description = "Total protein of the recipe.", example = "180")
    private BigDecimal totalProtein;

    @Schema(description = "Total sodium of the recipe.", example = "0")
    private BigDecimal totalSodium;

    @Schema(description = "Total salt of the recipe.", example = "60")
    private BigDecimal totalSalt;

    @Schema(description = "Total water of the recipe.", example = "95")
    private BigDecimal totalWater;

}
