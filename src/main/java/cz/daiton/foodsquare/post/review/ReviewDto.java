package cz.daiton.foodsquare.post.review;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ReviewDto {

    private final String required = "This field is required.";

    @NotEmpty(message = required)
    private String header;

    @NotEmpty(message = required)
    private String content;

    @NotNull(message = required)
    @DecimalMin(value = "1.00", message = "You must rate at least 1 star.")
    @DecimalMax(value = "5.00", message = "You cannot rate more than 5 stars.")
    @Digits(integer = 1, fraction = 0, message = "Rating cannot be a decimal number.")
    private BigDecimal rating;

    //TODO: multipartFile pro obrázky

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
