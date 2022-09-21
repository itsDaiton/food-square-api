package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.recipe.Recipe;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Review {

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
    private String text;

    @Column(nullable = false)
    @NotNull(message = required)
    @DecimalMin(value = "1.00", message = "You must rate at least 1 star.")
    @DecimalMax(value = "5.00", message = "You cannot rate more than 5 stars.")
    @Digits(integer = 1, fraction = 0, message = "Rating cannot be a decimal number.")
    private BigDecimal rating;

    @Column(
            name = "path_to_image"
    )
    private String pathToImage;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}