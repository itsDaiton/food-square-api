package cz.daiton.foodsquare.post.thread;

import java.util.List;

public interface ThreadService {

    Thread get(Long id);

    List<Thread> getAll();

    Thread add(ThreadDto threadDto);

    void update(ThreadDto threadDto, Long id);

    void delete(Long id);

    Thread findTopByOrderByIdDesc();
}
