package cz.daiton.foodsquare.authentication;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.authentication.request.LoginRequestDTO;
import cz.daiton.foodsquare.authentication.request.RegisterRequestDTO;
import cz.daiton.foodsquare.exceptions.CustomFieldError;
import cz.daiton.foodsquare.payload.response.FieldErrorResponse;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.payload.response.UserDataResponse;
import cz.daiton.foodsquare.role.Role;
import cz.daiton.foodsquare.role.RoleRepository;
import cz.daiton.foodsquare.role.RoleType;
import cz.daiton.foodsquare.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AppUserService appUserService;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> register(RegisterRequestDTO registerRequestDTO) {
        List<CustomFieldError> errorList = new ArrayList<>();
        FieldErrorResponse response = new FieldErrorResponse();

        if (appUserService.existsByUserName(registerRequestDTO.getUsername())) {
            CustomFieldError error = new CustomFieldError();
            error.setMessage("This username is already taken.");
            error.setField("username");
            errorList.add(error);
        }

        if (appUserService.existsByEmail(registerRequestDTO.getEmail())) {
            CustomFieldError error = new CustomFieldError();
            error.setMessage("This e-mail is already taken.");
            error.setField("email");
            errorList.add(error);
        }

        response.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        AppUser appUser = new AppUser(
                registerRequestDTO.getEmail(),
                registerRequestDTO.getUsername(),
                passwordEncoder.encode(registerRequestDTO.getPassword())
        );

        Set<String> strRoles = registerRequestDTO.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role was not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role roleAdmin = roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role was not found."));
                        roles.add(roleAdmin);
                        break;

                    case "moderator":
                        Role roleModerator = roleRepository.findByName(RoleType.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Role was not found."));
                        roles.add(roleModerator);
                        break;

                    default:
                        Role roleUser = roleRepository.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role was not found."));
                        roles.add(roleUser);
                }
            });
        }

        appUser.setRoles(roles);
        appUserService.register(appUser);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("You have been registered successfully."));
    }

    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie cookie = jwtUtils.generateCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UserDataResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                ));
    }

    @Override
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        ResponseCookie cookie = jwtUtils.cleanCookie();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You have been logged out successfully."));
    }
}
