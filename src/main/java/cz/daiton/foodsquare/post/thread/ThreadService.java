package cz.daiton.foodsquare.post.thread;

import java.util.List;

public interface ThreadService {
    //TODO: metody pro práce s databází

    Thread get(Long id);

    List<Thread> getAll();

    void add(Thread thread);

    void update(ThreadDto threadDto, Long id);

    void delete(Long id);
}
