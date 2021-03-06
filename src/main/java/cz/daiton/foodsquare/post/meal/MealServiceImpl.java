package cz.daiton.foodsquare.post.meal;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public Meal get(Long id) {
        return mealRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Meal> getAll() {
        return mealRepository.findAll();
    }

    @Override
    public void add(Meal meal) {
        mealRepository.save(meal);
    }

    @Override
    public void update(MealDto mealDto, Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow(NoSuchElementException::new);
        meal.setName(mealDto.getName());
        meal.setDescription(mealDto.getDescription());
        meal.setInstructions(mealDto.getInstructions());
        meal.setTimeToCook(mealDto.getTimeToCook());
        meal.setTimeToPrepare(mealDto.getTimeToPrepare());
        mealRepository.save(meal);
    }

    @Override
    public void delete(Long id) {
        mealRepository.deleteById(id);
    }
}
