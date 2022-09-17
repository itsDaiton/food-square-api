package cz.daiton.foodsquare.post.thread;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;


    public ThreadServiceImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    @Override
    public Thread get(Long id) {
        return threadRepository.findById(id).orElseThrow(NoSuchFieldError::new);
    }

    @Override
    public List<Thread> getAll() {
        return threadRepository.findAll();
    }

    @Override
    public Thread add(ThreadDto threadDto) {
        Thread thread = new Thread();

        thread.setHeader(threadDto.getHeader());
        thread.setContent(threadDto.getContent());

        return threadRepository.saveAndFlush(thread);
    }

    @Override
    public void update(ThreadDto threadDto, Long id) {
        Thread thread = threadRepository.findById(id).orElseThrow(NoSuchElementException::new);

        thread.setHeader(threadDto.getHeader());
        thread.setContent(threadDto.getContent());

        threadRepository.save(thread);
    }

    @Override
    public void delete(Long id) {
        threadRepository.deleteById(id);
    }

    @Override
    public Thread findTopByOrderByIdDesc() {
        return threadRepository.findTopByOrderByIdDesc().orElseThrow(NoSuchElementException::new);
    }
}
