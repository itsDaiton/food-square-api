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
    public Meal add(MealDto mealDto) {
        Meal meal = new Meal();

        meal.setName(mealDto.getName());
        meal.setDescription(mealDto.getDescription());
        meal.setInstructions(mealDto.getInstructions());
        meal.setTimeToCook(mealDto.getTimeToCook());
        meal.setTimeToPrepare(mealDto.getTimeToPrepare());

        return mealRepository.saveAndFlush(meal);
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

    @Override
    public Meal findTopByOrderByIdDesc() {
        return mealRepository.findTopByOrderByIdDesc().orElseThrow(NoSuchElementException::new);
    }
}
