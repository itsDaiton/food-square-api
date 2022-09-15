package cz.daiton.foodsquare.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

}
