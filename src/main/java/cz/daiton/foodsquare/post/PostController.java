package cz.daiton.foodsquare.post;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/posts")
@CrossOrigin
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
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
    public String addPost(@RequestBody PostDto postDto) {
        postService.add(postDto);
        return "Post has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updatePost(@RequestBody PostDto postDto, @PathVariable Long id) {
        postService.update(postDto, id);
        return "Post has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id);
        return "Post has been successfully deleted.";
    }

    //TODO: ošetřit vyjímky, práci s databází a securtnout endpointy
}
