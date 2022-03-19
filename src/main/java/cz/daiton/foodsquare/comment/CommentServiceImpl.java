package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.post.Post;
import cz.daiton.foodsquare.post.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AppUserRepository appUserRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment get(Long id) {
        return commentRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public void add(CommentDto commentDto) {
        Comment comment = new Comment();
        AppUser appUser = appUserRepository.findById(commentDto.getAppUser()).orElseThrow(NoSuchElementException::new);
        Post post = postRepository.findById(commentDto.getPost()).orElseThrow(NoSuchElementException::new);

        comment.setAppUser(appUser);
        comment.setPost(post);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setText(commentDto.getText());

        commentRepository.save(comment);
    }

    @Override
    public void update(CommentDto commentDto, Long id) {
        AppUser appUser = appUserRepository.findById(commentDto.getAppUser()).orElseThrow(NoSuchElementException::new);
        Post post = postRepository.findById(commentDto.getPost()).orElseThrow(NoSuchElementException::new);
        Comment comment = commentRepository.findById(id).orElseThrow(NoSuchElementException::new);

        comment.setAppUser(appUser);
        comment.setPost(post);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setText(commentDto.getText());

        commentRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
