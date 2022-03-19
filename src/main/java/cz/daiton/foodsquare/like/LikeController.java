package cz.daiton.foodsquare.like;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/likes")
@CrossOrigin
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping(value = "get/{id}")
    public Like getLike(@PathVariable Long id) {
        return likeService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Like> getLikes() {
        return likeService.getAll();
    }

    @PostMapping(value = "/add")
    public String addLike(@RequestBody LikeDto likeDto) {
        likeService.add(likeDto);
        return "Like has been successfully added.";
    }

    @PutMapping(value = "/update/{id}")
    public String updateLike(@RequestBody LikeDto likeDto, @PathVariable Long id) {
        likeService.update(likeDto, id);
        return "Like has been successfully updated.";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteLike(@PathVariable Long id) {
        likeService.delete(id);
        return "Like has been successfully deleted.";
    }
}
