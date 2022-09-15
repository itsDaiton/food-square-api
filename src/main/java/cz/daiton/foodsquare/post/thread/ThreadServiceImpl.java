package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;

    private final AppUserRepository appUserRepository;

    public ThreadServiceImpl(ThreadRepository threadRepository, AppUserRepository appUserRepository) {
        this.threadRepository = threadRepository;
        this.appUserRepository = appUserRepository;
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
    public void add(ThreadDto threadDto) {
        Thread thread = new Thread();
        AppUser appUser = appUserRepository.findById(threadDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        thread.setHeader(threadDto.getHeader());
        thread.setContent(threadDto.getContent());
        thread.setAppUser(appUser);

        threadRepository.save(thread);
    }

    @Override
    public void update(ThreadDto threadDto, Long id) {
        Thread thread = threadRepository.findById(id).orElseThrow(NoSuchElementException::new);
        AppUser appUser = appUserRepository.findById(threadDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        thread.setHeader(threadDto.getHeader());
        thread.setContent(threadDto.getContent());
        thread.setAppUser(appUser);

        threadRepository.save(thread);
    }

    @Override
    public void delete(Long id) {
        threadRepository.deleteById(id);
    }

    @Override
    public Thread findTopByAppUserOrderByIdDesc(AppUser appUser) {
        return threadRepository.findTopByAppUserOrderByIdDesc(appUser).orElseThrow(NoSuchElementException::new);
    }
}
