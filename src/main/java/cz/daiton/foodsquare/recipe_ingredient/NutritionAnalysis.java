package cz.daiton.foodsquare.recipe_ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class NutritionAnalysis {

    private BigDecimal totalCalories;

    private BigDecimal totalFat;

    private BigDecimal totalSaturatedFattyAcids;

    private BigDecimal totalMonounsaturatedFattyAcids;

    private BigDecimal totalPolyunsaturatedFattyAcids;

    private BigDecimal totalTransFattyAcids;

    private BigDecimal totalCarbohydrateTotal;

    private BigDecimal totalCarbohydrateAvailable;

    private BigDecimal totalFibre;

    private BigDecimal totalSugar;

    private BigDecimal totalProtein;

    private BigDecimal totalSodium;

    private BigDecimal totalSalt;

    private BigDecimal totalWater;

}
