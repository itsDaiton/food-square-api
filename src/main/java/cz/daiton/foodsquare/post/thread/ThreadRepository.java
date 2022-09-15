package cz.daiton.foodsquare.post.thread;

import cz.daiton.foodsquare.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {

    Optional<Thread> findTopByAppUserOrderByIdDesc(AppUser appUser);

}
