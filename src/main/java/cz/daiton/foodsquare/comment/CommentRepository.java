package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByCommentedAtDesc();

    List<Comment> findAllByLikes(AppUser appUser);

    List<Comment> findAllByRecipe(Recipe recipe);

    Integer countAllByRecipe(Recipe recipe);

}
