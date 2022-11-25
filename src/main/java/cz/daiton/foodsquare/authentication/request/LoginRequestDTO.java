package cz.daiton.foodsquare.authentication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login request.")
public class LoginRequestDTO {

    @Schema(description = "Username of the user.", example = "user123")
    private String username;

    @Schema(description = "Password of the user.", example = "example123")
    public String password;

}
