package cz.daiton.foodsquare.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity(name = "review")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"app_user_id", "recipe_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Class representing a review.")
public class Review {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Schema(description = "Unique identifier for review.", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = required)
    @Schema(description = "Text of the review.", example = "example review text")
    private String text;

    @Column(nullable = false)
    @NotNull(message = required)
    @DecimalMin(value = "1.00", message = "You must rate at least 1 star.")
    @DecimalMax(value = "5.00", message = "You cannot rate more than 5 stars.")
    @Digits(integer = 1, fraction = 0, message = "Rating cannot be a decimal number.")
    @Schema(description = "Rating of the review.", example = "5")
    private BigDecimal rating;

    @Column(
            name = "updated_at",
            nullable = false
    )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Date of last update to the review.")
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