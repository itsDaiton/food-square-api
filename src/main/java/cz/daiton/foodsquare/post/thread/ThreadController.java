package cz.daiton.foodsquare.post.thread;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/threads")
@CrossOrigin
public class ThreadController {

    private final ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
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
    public String addThread(@RequestBody Thread thread) {
        threadService.add(thread);
        return "Thread has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updateThread(@RequestBody ThreadDto threadDto, @PathVariable Long id) {
        threadService.update(threadDto, id);
        return "Thread has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteThread(@PathVariable Long id) {
        threadService.delete(id);
        return "Thread has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy

}
