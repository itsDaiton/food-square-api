package cz.daiton.foodsquare.authentication;

import cz.daiton.foodsquare.authentication.request.LoginRequestDTO;
import cz.daiton.foodsquare.authentication.request.RegisterRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> register(RegisterRequestDTO registerRequestDTO);

    ResponseEntity<?> login(LoginRequestDTO loginRequestDTO);

    ResponseEntity<?> logout();
}
