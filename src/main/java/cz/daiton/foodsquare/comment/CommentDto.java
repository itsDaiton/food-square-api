package cz.daiton.foodsquare.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotEmpty(message = required)
    @Size(max = 500, message = "Comment can contain a maximum of 500 characters.")
    private String text;

    private Long appUser;

    private Long recipe;
}
