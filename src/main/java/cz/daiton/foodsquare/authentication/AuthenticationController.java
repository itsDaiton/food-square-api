package cz.daiton.foodsquare.authentication;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserServiceImpl;
import cz.daiton.foodsquare.authentication.request.LoginRequestDTO;
import cz.daiton.foodsquare.authentication.request.RegisterRequestDTO;
import cz.daiton.foodsquare.payload.response.FieldErrorResponse;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.payload.response.UserDataResponse;
import cz.daiton.foodsquare.role.AppUserRole;
import cz.daiton.foodsquare.role.Role;
import cz.daiton.foodsquare.role.RoleRepository;
import cz.daiton.foodsquare.exceptions.CustomFieldError;
import cz.daiton.foodsquare.security.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class AuthenticationController {

    AuthenticationManager authenticationManager;

    AppUserRepository appUserRepository;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    JwtUtils jwtUtils;

    AppUserServiceImpl appUserService;


    public AuthenticationController(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AppUserServiceImpl appUserService) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDto) {

        List<CustomFieldError> errorList = new ArrayList<>();
        FieldErrorResponse response = new FieldErrorResponse();

        if (appUserService.existsByUserName(registerRequestDto.getUsername())) {
            CustomFieldError error = new CustomFieldError();
            error.setMessage("This username is already taken.");
            error.setField("username");
            errorList.add(error);
        }

        if (appUserService.existsByEmail(registerRequestDto.getEmail())) {
            CustomFieldError error = new CustomFieldError();
            error.setMessage("This e-mail is already taken.");
            error.setField("email");
            errorList.add(error);
        }

        response.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        AppUser appUser = new AppUser(
                registerRequestDto.getEmail(),
                registerRequestDto.getUsername(),
                passwordEncoder.encode(registerRequestDto.getPassword())
        );

        Set<String> strRoles = registerRequestDto.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(AppUserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role roleAdmin = roleRepository.findByName(AppUserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(roleAdmin);
                        break;

                    case "moderator":
                        Role roleModerator = roleRepository.findByName(AppUserRole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(roleModerator);
                        break;

                    default:
                        Role roleUser = roleRepository.findByName(AppUserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(roleUser);
                }
            });
        }

        appUser.setRoles(roles);
        appUserService.register(appUser);

        return ResponseEntity.ok(new MessageResponse("User has been registered successfully."));
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDto) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
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

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        ResponseCookie cookie = jwtUtils.cleanCookie();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You have been logged out."));
    }
}
