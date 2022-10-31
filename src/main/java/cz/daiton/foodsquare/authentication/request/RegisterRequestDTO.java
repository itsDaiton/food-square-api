package cz.daiton.foodsquare.authentication.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class RegisterRequestDTO {

    private final String required = "This field is required.";

    @NotNull(message = required)
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters long.")
    private String username;

    @NotEmpty(message = required)
    @Email(message = "This is not valid e-mail address.")
    private String email;

    @NotNull(message = required)
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    private Set<String> role;

}
