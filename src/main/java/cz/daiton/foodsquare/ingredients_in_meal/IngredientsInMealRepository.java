package cz.daiton.foodsquare.ingredients_in_meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsInMealRepository extends JpaRepository<IngredientsInMeal, Long> {

}
