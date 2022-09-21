package cz.daiton.foodsquare.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
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
import java.util.Set;

@Entity(name = "recipe")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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
    @Min(value = 1, message = "Time to cook must be at least 1 minute.")
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
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<RecipeIngredient> ingredientSet = new HashSet<>();
}