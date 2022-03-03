package cz.daiton.foodsquare.post.meal;

import org.springframework.stereotype.Service;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    //TODO: implementace metod z rozhraní přes repository
}
