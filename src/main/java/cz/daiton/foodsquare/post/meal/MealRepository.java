package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Optional<Meal> findTopByAppUserOrderByIdDesc(AppUser appUser);

}
