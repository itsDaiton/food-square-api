package cz.daiton.foodsquare.appuser;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(value = "get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public AppUser getUser(@PathVariable Long id) {
        return appUserService.get(id);
    }


    @GetMapping(value = "/getAll")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<AppUser> getAllUsers() {
        return appUserService.getAll();
    }

    @PostMapping(value = "/add")
    public String addUser(@RequestBody AppUser appUser) {
        appUserService.add(appUser);
        return "User has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updateUser(@RequestBody AppUserDto appUserDto, @PathVariable Long id) {
        appUserService.update(appUserDto, id);
        return "User has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        appUserService.delete(id);
        return "User has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy


}
