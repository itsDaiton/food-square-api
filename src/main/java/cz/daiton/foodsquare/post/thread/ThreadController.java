package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/threads")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class ThreadController {

    private final ThreadService threadService;

    private final AppUserService appUserService;

    public ThreadController(ThreadService threadService, AppUserService appUserService) {
        this.threadService = threadService;
        this.appUserService = appUserService;
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
    public ResponseEntity<?> addThread(@RequestBody ThreadDto threadDto) {
        /*
        threadService.add(threadDto);
        AppUser appUser = appUserService.get(threadDto.getAppUser());
        Thread thread = threadService.findTopByAppUserOrderByIdDesc(appUser);
        return ResponseEntity
                .ok()
                .body(new PostContentResponse(
                        thread.getId(),
                        appUser.getId(),
                        "Thread has been successfully added."
                ));
         */
        //TODO: vyřešit
        return ResponseEntity.ok().body("placeholder");
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
