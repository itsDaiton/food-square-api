package cz.daiton.foodsquare.meal_planning;

import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.category.CategoryRepository;
import cz.daiton.foodsquare.category.CategoryType;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeMeal;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import cz.daiton.foodsquare.recipe_ingredient.NutritionAnalysis;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MealPlanServiceImpl implements MealPlanService {

    private final RecipeIngredientService recipeIngredientService;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public MealPlanOutput generateMealPlan(MealPlanRequestDto dto) {
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            throw new RuntimeException("Unable to create a meal plan. No recipes found.");
        }
        List<Recipe> recipesFiltered = new ArrayList<>();
        Set<Category> categories = new HashSet<>();

        if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
            for (String name : dto.getCategories()) {
                Category category = categoryRepository.findByName(CategoryType.valueOf(name)).orElseThrow(
                        () -> new NoSuchElementException("Category: '" + CategoryType.valueOf(name) + "' was not found.")
                );
                categories.add(category);
            }
            for (Recipe r : recipes) {
                if (r.getCategories().containsAll(categories)) {
                    recipesFiltered.add(r);
                }
            }
        }
        else {
            recipesFiltered.addAll(recipes);
        }

        if (recipesFiltered.isEmpty()) {
            throw new RuntimeException("Unable to create a meal plan. Please try different set of inputs.");
        }

        Collections.shuffle(recipesFiltered);

        List<Recipe> breakfasts = recipesFiltered.stream()
                .filter(r -> r.getMeal().equals(RecipeMeal.BREAKFAST)).collect(Collectors.toList());
        List<Recipe> lunches = recipesFiltered.stream()
                .filter(r -> r.getMeal().equals(RecipeMeal.LUNCH)).collect(Collectors.toList());
        List<Recipe> dinners = recipesFiltered.stream()
                .filter(r -> r.getMeal().equals(RecipeMeal.DINNER)).collect(Collectors.toList());
        List<Recipe> snacks = recipesFiltered.stream()
                .filter(r -> r.getMeal().equals(RecipeMeal.SNACK)).collect(Collectors.toList());

        List<NutritionAnalysis> analyses = new ArrayList<>();
        List<Recipe> recipeOutputs = new ArrayList<>();
        MealPlanOutput output = new MealPlanOutput();



        switch (dto.getAmount().intValue()) {
            case 1:
                for (Recipe r : recipesFiltered) {
                    NutritionAnalysis values = recipeIngredientService.getNutritionAnalysis(r);
                    if (
                    values.getTotalCalories().doubleValue() < (dto.getCalories().doubleValue() + 100)  &&
                    values.getTotalCalories().doubleValue() > (dto.getCalories().doubleValue() - 100)
                    ) {
                        recipeOutputs.add(r);
                        output.setRecipes(recipeOutputs);
                        output.setAnalysis(values);
                        return output;
                    }
                }
                break;
            case 2:
                for (Recipe b : breakfasts) {
                    for (Recipe l : lunches) {
                        NutritionAnalysis bValues = recipeIngredientService.getNutritionAnalysis(b);
                        NutritionAnalysis lValues = recipeIngredientService.getNutritionAnalysis(l);
                        if (
                        bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() < (dto.getCalories().doubleValue() + 100) &&
                        bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() > (dto.getCalories().doubleValue() - 100)
                        ) {
                            recipeOutputs.add(b);
                            recipeOutputs.add(l);
                            analyses.add(bValues);
                            analyses.add(lValues);
                            output.setRecipes(recipeOutputs);
                            output.setAnalysis(summariseNutritionAnalyses(analyses));
                            return output;
                        }
                    }
                }
                break;
            case 3:
                for (Recipe b : breakfasts) {
                    for (Recipe l : lunches) {
                        for (Recipe d : dinners) {
                            NutritionAnalysis bValues = recipeIngredientService.getNutritionAnalysis(b);
                            NutritionAnalysis lValues = recipeIngredientService.getNutritionAnalysis(l);
                            NutritionAnalysis dValues = recipeIngredientService.getNutritionAnalysis(d);
                            if (
                            bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() + dValues.getTotalCalories().doubleValue() < (dto.getCalories().doubleValue() + 100) &&
                            bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() + dValues.getTotalCalories().doubleValue() > (dto.getCalories().doubleValue() - 100)
                            ) {
                                recipeOutputs.add(b);
                                recipeOutputs.add(l);
                                recipeOutputs.add(d);
                                analyses.add(bValues);
                                analyses.add(lValues);
                                analyses.add(dValues);
                                output.setRecipes(recipeOutputs);
                                output.setAnalysis(summariseNutritionAnalyses(analyses));
                                return output;
                            }
                        }
                    }
                }
                break;
            case 4:
                for (Recipe b : breakfasts) {
                    for (Recipe l : lunches) {
                        for (Recipe d : dinners) {
                            for (Recipe s : snacks) {
                                NutritionAnalysis bValues = recipeIngredientService.getNutritionAnalysis(b);
                                NutritionAnalysis lValues = recipeIngredientService.getNutritionAnalysis(l);
                                NutritionAnalysis dValues = recipeIngredientService.getNutritionAnalysis(d);
                                NutritionAnalysis sValues = recipeIngredientService.getNutritionAnalysis(s);
                                if (
                                bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() + dValues.getTotalCalories().doubleValue() + sValues.getTotalCalories().doubleValue() < (dto.getCalories().doubleValue() + 100) &&
                                bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() + dValues.getTotalCalories().doubleValue() + sValues.getTotalCalories().doubleValue() > (dto.getCalories().doubleValue() - 100)
                                ) {
                                    recipeOutputs.add(b);
                                    recipeOutputs.add(l);
                                    recipeOutputs.add(d);
                                    recipeOutputs.add(s);
                                    analyses.add(bValues);
                                    analyses.add(lValues);
                                    analyses.add(dValues);
                                    analyses.add(sValues);
                                    output.setRecipes(recipeOutputs);
                                    output.setAnalysis(summariseNutritionAnalyses(analyses));
                                    return output;
                                }
                            }
                        }
                    }
                }
                break;
            case 5:
                for (Recipe b : breakfasts) {
                    for (Recipe l : lunches) {
                        for (Recipe d : dinners) {
                            for (Recipe s1 : snacks) {
                                for (Recipe s2 : snacks) {
                                    NutritionAnalysis bValues = recipeIngredientService.getNutritionAnalysis(b);
                                    NutritionAnalysis lValues = recipeIngredientService.getNutritionAnalysis(l);
                                    NutritionAnalysis dValues = recipeIngredientService.getNutritionAnalysis(d);
                                    NutritionAnalysis s1Values = recipeIngredientService.getNutritionAnalysis(s1);
                                    NutritionAnalysis s2Values = recipeIngredientService.getNutritionAnalysis(s2);
                                    if (
                                    bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() + dValues.getTotalCalories().doubleValue() + s1Values.getTotalCalories().doubleValue() + s2Values.getTotalCalories().doubleValue() < (dto.getCalories().doubleValue() + 100) &&
                                    bValues.getTotalCalories().doubleValue() + lValues.getTotalCalories().doubleValue() + dValues.getTotalCalories().doubleValue() + s1Values.getTotalCalories().doubleValue() + s2Values.getTotalCalories().doubleValue() > (dto.getCalories().doubleValue() - 100) &&
                                    !s1.getId().equals(s2.getId())
                                    ) {
                                        recipeOutputs.add(b);
                                        recipeOutputs.add(l);
                                        recipeOutputs.add(d);
                                        recipeOutputs.add(s1);
                                        recipeOutputs.add(s2);
                                        analyses.add(bValues);
                                        analyses.add(lValues);
                                        analyses.add(dValues);
                                        analyses.add(s1Values);
                                        analyses.add(s2Values);
                                        output.setRecipes(recipeOutputs);
                                        output.setAnalysis(summariseNutritionAnalyses(analyses));
                                        return output;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException("Unable to generate a meal plan with more than 5 meals.");
        }
        throw new RuntimeException("Unable to create a meal plan. Please try different set of inputs.");
    }

    private NutritionAnalysis summariseNutritionAnalyses(List<NutritionAnalysis> nutritionAnalyses) {
        double totalCalories = 0;
        double totalFat = 0;
        double totalSaturatedFattyAcids = 0;
        double totalMonounsaturatedFattyAcids = 0;
        double totalPolyunsaturatedFattyAcids = 0;
        double totalTransFattyAcids = 0;
        double totalCarbohydrateTotal = 0;
        double totalCarbohydrateAvailable = 0;
        double totalFibre = 0;
        double totalSugar = 0;
        double totalProtein = 0;
        double totalSodium = 0;
        double totalSalt = 0;
        double totalWater = 0;

        for (NutritionAnalysis n : nutritionAnalyses) {
            totalCalories += n.getTotalCalories().doubleValue();
            totalFat += n.getTotalFat().doubleValue();
            totalSaturatedFattyAcids += n.getTotalSaturatedFattyAcids().doubleValue();
            totalMonounsaturatedFattyAcids += n.getTotalMonounsaturatedFattyAcids().doubleValue();
            totalPolyunsaturatedFattyAcids += n.getTotalPolyunsaturatedFattyAcids().doubleValue();
            totalTransFattyAcids += n.getTotalTransFattyAcids().doubleValue();
            totalCarbohydrateTotal += n.getTotalCarbohydrateTotal().doubleValue();
            totalCarbohydrateAvailable += n.getTotalCarbohydrateAvailable().doubleValue();
            totalFibre += n.getTotalFibre().doubleValue();
            totalSugar += n.getTotalSugar().doubleValue();
            totalProtein += n.getTotalProtein().doubleValue();
            totalSodium += n.getTotalSodium().doubleValue();
            totalSalt += n.getTotalSalt().doubleValue();
            totalWater += n.getTotalWater().doubleValue();
        }

        return new NutritionAnalysis(
                BigDecimal.valueOf(totalCalories).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalFat).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSaturatedFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalMonounsaturatedFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalPolyunsaturatedFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalTransFattyAcids).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalCarbohydrateTotal).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalCarbohydrateAvailable).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalFibre).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSugar).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalProtein).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSodium).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalSalt).setScale(1, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(totalWater).setScale(1, RoundingMode.HALF_EVEN)
        );
    }
}
