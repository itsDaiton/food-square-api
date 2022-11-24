package cz.daiton.foodsquare.authentication;

import cz.daiton.foodsquare.authentication.request.LoginRequestDTO;
import cz.daiton.foodsquare.authentication.request.RegisterRequestDTO;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/auth")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://food-square.site/"},
        maxAge = 3600,
        allowCredentials = "true"
)
@AllArgsConstructor
@Tag(description = "Set of endpoints for user authentication.", name = "Authentication Controller")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Creates a user.")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDto) {
        return authenticationService.register(registerRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Makes a login request.")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
        return authenticationService.login(loginRequestDto);
    }

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Makes a logout request.")
    public ResponseEntity<?> logoutUser() {
        return authenticationService.logout();
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    NumberFormatException.class,
                    InvalidDataAccessApiUsageException.class,
                    HttpRequestMethodNotSupportedException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "Wrong request method. Please try again.";
        }
        else if (e instanceof NumberFormatException) {
            message = "Please enter a valid number.";
        }
        else {
            message = e.getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
