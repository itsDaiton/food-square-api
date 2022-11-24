package cz.daiton.foodsquare.authentication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Register request.")
public class RegisterRequestDTO {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @NotNull(message = required)
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters long.")
    @Schema(description = "Username of the user.", example = "user123")
    private String username;

    @NotEmpty(message = required)
    @Email(message = "This is not valid e-mail address.")
    @Schema(description = "Email of the user.", example = "user@example.com")
    private String email;

    @NotNull(message = required)
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Schema(description = "Password of the user.", example = "example123")
    private String password;

    @Schema(hidden = true)
    private Set<String> role;

}
