package cz.daiton.foodsquare.review;

import cz.daiton.foodsquare.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByUpdatedAtDesc();

    List<Review> findAllByLikes(AppUser appUser);

}
