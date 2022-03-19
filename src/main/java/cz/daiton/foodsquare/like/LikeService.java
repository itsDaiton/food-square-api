package cz.daiton.foodsquare.like;

import java.util.List;

public interface LikeService {

    Like get(Long id);

    List<Like> getAll();

    void add(LikeDto likeDto);

    void update(LikeDto likeDto, Long id);

    void delete(Long id);

}
