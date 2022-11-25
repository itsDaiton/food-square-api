package cz.daiton.foodsquare.appuser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Object representing a relation between user and a content (review or comment).")
public class LikeDto {

    @Schema(description = "ID of the user.", example = "1")
    private Long appUser;

    @Schema(description = "ID of the content.", example = "1")
    private Long content;

    @Schema(description = "Type of the content.", example = "review")
    private String type;

}
