package cz.daiton.foodsquare.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Object representing a review.")
public class ReviewDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotEmpty(message = required)
    @Schema(description = "Text of the review.", example = "example review text")
    private String text;

    @NotNull(message = required)
    @DecimalMin(value = "1.00", message = "You must rate at least 1 star.")
    @DecimalMax(value = "5.00", message = "You cannot rate more than 5 stars.")
    @Digits(integer = 1, fraction = 0, message = "Rating cannot be a decimal number.")
    @Schema(description = "Rating of the review.", example = "5")
    private BigDecimal rating;

    @Schema(description = "ID of the user.", example = "1")
    private Long appUser;

    @Schema(description = "ID of the recipe.", example = "1")
    private Long recipe;
}
