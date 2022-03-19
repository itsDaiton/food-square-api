package cz.daiton.foodsquare.comment;

import java.util.List;

public interface CommentService {

    Comment get(Long id);

    List<Comment> getAll();

    void add(CommentDto commentDto);

    void update(CommentDto commentDto, Long id);

    void delete(Long id);
}
