package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.category.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class RecipeExtended {

    private Long id;

    private String name;

    private String description;

    private String instructions;

    private Integer timeToPrepare;

    private Integer timeToCook;

    private String pathToImage;

    private LocalDateTime updatedAt;

    private AppUser appUser;

    private Set<Category> categories;

    private RecipeMeal meal;

    private BigDecimal avgRating;
}
