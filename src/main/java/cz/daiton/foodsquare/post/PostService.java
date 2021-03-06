package cz.daiton.foodsquare.post;

import java.util.List;

public interface PostService {

    Post get(Long id);

    List<Post> getAll();

    void add(PostDto postDto);

    void update(PostDto postDto, Long id);

    void delete(Long id);
}
