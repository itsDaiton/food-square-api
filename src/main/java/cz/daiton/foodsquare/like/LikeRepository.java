package cz.daiton.foodsquare.like;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByOrderByLikedAtDesc();

    boolean existsByAppUserAndPost(AppUser appUser, Post post);

}
