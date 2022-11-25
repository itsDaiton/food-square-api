package cz.daiton.foodsquare.appuser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Object representing a relation between user and a recipe.")
public class FavoriteDto {

    @Schema(description = "ID of the user.", example = "1")
    private Long appUser;

    @Schema(description = "ID of the recipe.", example = "1")
    private Long recipe;
}
