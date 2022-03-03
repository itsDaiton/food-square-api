package cz.daiton.foodsquare.post.thread;

import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;

    public ThreadServiceImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    //TODO: implementace metod z rozhraní přes repository
}
