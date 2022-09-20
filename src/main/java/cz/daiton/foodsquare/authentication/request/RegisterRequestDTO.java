package cz.daiton.foodsquare.authentication.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
