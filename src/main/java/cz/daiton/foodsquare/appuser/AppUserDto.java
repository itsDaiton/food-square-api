package cz.daiton.foodsquare.appuser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Object representing user's personal information.")
public class AppUserDto {

    @Schema(description = "First name of the user.", example = "John")
    private String firstName;

    @Schema(description = "Last name of the user.", example = "Doe")
    private String lastName;

}
