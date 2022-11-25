package cz.daiton.foodsquare.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "Object representing comment entity.")
public class CommentDto {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotEmpty(message = required)
    @Size(max = 500, message = "Comment can contain a maximum of 500 characters.")
    @Schema(description = "Text of the comment.", example = "example text")
    private String text;

    @Schema(description = "ID of the user.", example = "1")
    private Long appUser;

    @Schema(description = "ID of the recipe.", example = "1")
    private Long recipe;
}
