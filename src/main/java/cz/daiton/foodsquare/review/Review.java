package cz.daiton.foodsquare.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.recipe.Recipe;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @ManyToMany(mappedBy = "likedReviews")
    @JsonIgnore
    Set<AppUser> likes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}