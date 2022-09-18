package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.payload.response.PostContentResponse;
import cz.daiton.foodsquare.security.IncorrectUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<?> addThread(@Valid @RequestBody ThreadDto threadDto, HttpServletRequest request) throws Exception{
        Thread thread = threadService.add(threadDto);
        return ResponseEntity
                .ok()
                .body(new PostContentResponse(
                        thread.getId(),
                        appUserService.getLocalUser(request).getId(),
                        "Thread has been successfully added."
                ));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateThread(@Valid @RequestBody ThreadDto threadDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        threadService.update(threadDto, id, request);
        return ResponseEntity
                .ok()
                .body(new MessageResponse(threadService.update(threadDto, id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }

}
