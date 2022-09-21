package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFollowedId(Long id);

    List<Follow> findAllByFollowerId(Long id);

    Boolean existsByFollowerAndFollowed(AppUser follower, AppUser followed);
}
