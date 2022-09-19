package cz.daiton.foodsquare.post;

import cz.daiton.foodsquare.payload.response.MessageResponse;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
                .body(new MessageResponse(postService.add(postDto, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePost(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(postService.delete(id, request)));
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Post must have a user, it cannot be null.";
        }
        else {
            message = e.getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
