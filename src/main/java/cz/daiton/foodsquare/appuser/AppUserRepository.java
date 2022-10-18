package cz.daiton.foodsquare.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM app_user ORDER BY random() LIMIT 5")
    List<AppUser> find5RandomUsers();
    
}
