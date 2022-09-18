package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.post.Post;
import cz.daiton.foodsquare.post.PostRepository;
import cz.daiton.foodsquare.post.PostService;
import cz.daiton.foodsquare.security.IncorrectUserException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;
    private final PostService postService;
    private final PostRepository postRepository;


    public ThreadServiceImpl(ThreadRepository threadRepository, PostService postService, PostRepository postRepository) {
        this.threadRepository = threadRepository;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @Override
    public Thread get(Long id) {
        return threadRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Thread with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Thread> getAll() {
        return threadRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Thread add(ThreadDto threadDto) {
        Thread thread = new Thread();

        thread.setHeader(threadDto.getHeader());
        thread.setContent(threadDto.getContent());

        return threadRepository.saveAndFlush(thread);
    }

    @Override
    public String update(ThreadDto threadDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Thread thread = threadRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Thread with id: '" + id + "' does not exist.")
        );
        Post post = postRepository.findByThread(thread).orElseThrow(
                () -> new NoSuchElementException("Post with thread with id: '" + id + "' has not been found.")
        );

        if (postService.checkUser(post.getAppUser().getId(), request)) {
            thread.setHeader(threadDto.getHeader());
            thread.setContent(threadDto.getContent());

            threadRepository.save(thread);
            return "Thread has been successfully updated.";
        }
        return "There has been a error while trying to update the review.";
    }
}
