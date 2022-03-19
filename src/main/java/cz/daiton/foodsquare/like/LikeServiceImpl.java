package cz.daiton.foodsquare.like;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.post.Post;
import cz.daiton.foodsquare.post.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;

    public LikeServiceImpl(LikeRepository likeRepository, AppUserRepository appUserRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Like get(Long id) {
        return likeRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public void add(LikeDto likeDto) {
        Like like = new Like();
        AppUser appUser = appUserRepository.findById(likeDto.getAppUser()).orElseThrow(NoSuchElementException::new);
        Post post = postRepository.findById(likeDto.getPost()).orElseThrow(NoSuchElementException::new);

        like.setAppUser(appUser);
        like.setPost(post);
        like.setLikedAt(LocalDateTime.now());

        likeRepository.save(like);
    }

    @Override
    public void update(LikeDto likeDto, Long id) {
        AppUser appUser = appUserRepository.findById(likeDto.getAppUser()).orElseThrow(NoSuchElementException::new);
        Post post = postRepository.findById(likeDto.getPost()).orElseThrow(NoSuchElementException::new);
        Like like = likeRepository.findById(id).orElseThrow(NoSuchElementException::new);

        like.setAppUser(appUser);
        like.setPost(post);
        like.setLikedAt(LocalDateTime.now());

        likeRepository.save(like);
    }

    @Override
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }
}
