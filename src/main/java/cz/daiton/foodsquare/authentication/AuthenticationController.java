package cz.daiton.foodsquare.authentication;

import cz.daiton.foodsquare.authentication.request.LoginRequestDTO;
import cz.daiton.foodsquare.authentication.request.RegisterRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDto) {
        return authenticationService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
        return authenticationService.login(loginRequestDto);
    }

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser() {
        return authenticationService.logout();
    }
}
