package cz.daiton.foodsquare.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredient;
import cz.daiton.foodsquare.review.Review;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "recipe")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recipe {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String description;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String instructions;

    @Column(
            name = "prep_time",
            nullable = false
    )
    @NotNull(message = required)
    @Min(value = 1, message = "Preparation time must be at least 1 minute.")
    private Integer timeToPrepare;

    @Column(
            name = "cook_time",
            nullable = false
    )
    @NotNull(message = required)
    @Min(value = 1, message = "Cooking time must be at least 1 minute.")
    private Integer timeToCook;

    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "app_user_id",
            nullable = false,
            updatable = false
    )
    private AppUser appUser;

    @OneToMany(
            mappedBy = "recipe"
    )
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(
            mappedBy = "recipe"
    )
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(
            mappedBy = "recipe"
    )
    @JsonIgnore
    private Set<RecipeIngredient> ingredientSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "recipe_categories",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(
            mappedBy = "favoriteRecipes"
    )
    @JsonIgnore
    private Set<AppUser> favorites;

    @Enumerated(EnumType.STRING)
    @NotNull(message = required)
    private RecipeMeal meal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
