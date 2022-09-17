package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import cz.daiton.foodsquare.security.IncorrectUserException;
import cz.daiton.foodsquare.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/threads")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class ThreadController {

    private final ThreadService threadService;

    private final AppUserService appUserService;

    private final JwtUtils jwtUtils;

    public ThreadController(ThreadService threadService, AppUserService appUserService, JwtUtils jwtUtils) {
        this.threadService = threadService;
        this.appUserService = appUserService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(value = "get/{id}")
    public Thread getThread(@PathVariable Long id) {
        return threadService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Thread> getAllThreads() {
        return threadService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addThread(@RequestBody ThreadDto threadDto, HttpServletRequest request) throws IncorrectUserException{
        Thread thread = threadService.add(threadDto);

        String jwt = jwtUtils.getJwtFromCookies(request);

        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            AppUser appUser = appUserService.findByUsername(username);
            return ResponseEntity
                    .ok()
                    .body(new PostContentResponse(
                            thread.getId(),
                            appUser.getId(),
                            "Thread has been successfully added."
                    ));
        }
        else {
            throw new IncorrectUserException("You cannot do this. You are not the same user.");
        }
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateThread(@RequestBody ThreadDto threadDto, @PathVariable Long id) {
        threadService.update(threadDto, id);
        return "Thread has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteThread(@PathVariable Long id) {
        threadService.delete(id);
        return "Thread has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy

}
