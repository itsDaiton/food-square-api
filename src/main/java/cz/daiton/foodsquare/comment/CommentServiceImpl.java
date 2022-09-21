package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;
    private final RecipeRepository recipeRepository;
    private final AppUserService appUserService;

    @Override
    public Comment get(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Comment with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAllByOrderByCommentedAtDesc();
    }

    @Override
    public String add(CommentDto commentDto, HttpServletRequest request) throws IncorrectUserException {
        Comment comment = new Comment();
        AppUser appUser = appUserRepository.findById(commentDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + commentDto.getAppUser() + "' does not exist.")
        );
        Recipe recipe = recipeRepository.findById(commentDto.getRecipe()).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + commentDto.getRecipe() + "' does not exist.")
        );

        if (appUserService.checkUser(appUser.getId(), request)) {
            comment.setAppUser(appUser);
            comment.setRecipe(recipe);
            comment.setCommentedAt(LocalDateTime.now());
            comment.setText(commentDto.getText());

            commentRepository.save(comment);
            return "Comment has been successfully created.";
        }
        return "There has been a error while trying to add the comment.";
    }

    @Override
    public String update(CommentDto commentDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Comment with id: '" + id + "' does not exist. You cannot edit it.")
        );

        if (appUserService.checkUser(comment.getAppUser().getId(), request)) {
            comment.setCommentedAt(LocalDateTime.now());
            comment.setText(commentDto.getText());
            commentRepository.save(comment);

            return "Comment has been successfully updated.";
        }
        return "There has been a error while trying to edit the comment.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Comment with id: '" + id + "' does not exist. You cannot delete it.")
        );

        if (appUserService.checkUser(comment.getAppUser().getId(), request)) {
            commentRepository.deleteById(id);
            return "Comment has been successfully deleted.";
        }
        return "There has been a error while trying to delete the comment.";
    }
}
