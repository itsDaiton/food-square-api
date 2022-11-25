package cz.daiton.foodsquare.follow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Object representing follow entity.")
public class FollowDto {

    @Schema(description = "ID of the follower.", example = "1")
    private Long follower;

    @Schema(description = "ID of the followed user.", example = "1")
    private Long followed;
}
