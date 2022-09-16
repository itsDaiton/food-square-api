package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import cz.daiton.foodsquare.security.IncorrectUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/posts")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class PostController {

    private final PostService postService;

    public PostController( PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "get/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(postService.handleRequest(postDto, request)));
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updatePost(@RequestBody PostDto postDto, @PathVariable Long id) {
        postService.update(postDto, id);
        return "Post has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id);
        return "Post has been successfully deleted.";
    }

    @ExceptionHandler(value = {IncorrectUserException.class, RuntimeException.class})
    public ResponseEntity<?> handleExceptions(Exception e) {
        if (e instanceof IncorrectUserException) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
        if (e instanceof RuntimeException) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy
}
