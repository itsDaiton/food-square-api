package cz.daiton.foodsquare.comment;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/comments")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "get/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Comment> getComments() {
        return commentService.getAll();
    }

    @PostMapping(value = "/add")
    public String addComment(@RequestBody CommentDto commentDto) {
        commentService.add(commentDto);
        return "Comment has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updateComment(@RequestBody CommentDto commentDto, @PathVariable Long id) {
        commentService.update(commentDto, id);
        return "Comment has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return "Comment has been successfully deleted.";
    }

}
