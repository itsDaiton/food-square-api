package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    private final AppUserRepository appUserRepository;

    public MealServiceImpl(MealRepository mealRepository, AppUserRepository appUserRepository) {
        this.mealRepository = mealRepository;
        this.appUserRepository = appUserRepository;
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
    public void add(MealDto mealDto) {
        Meal meal = new Meal();
        AppUser appUser = appUserRepository.findById(mealDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        meal.setName(mealDto.getName());
        meal.setDescription(mealDto.getDescription());
        meal.setInstructions(mealDto.getInstructions());
        meal.setTimeToCook(mealDto.getTimeToCook());
        meal.setTimeToPrepare(mealDto.getTimeToPrepare());
        meal.setAppUser(appUser);

        mealRepository.save(meal);
    }

    @Override
    public void update(MealDto mealDto, Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow(NoSuchElementException::new);
        AppUser appUser = appUserRepository.findById(mealDto.getAppUser()).orElseThrow(NoSuchElementException::new);

        meal.setName(mealDto.getName());
        meal.setDescription(mealDto.getDescription());
        meal.setInstructions(mealDto.getInstructions());
        meal.setTimeToCook(mealDto.getTimeToCook());
        meal.setTimeToPrepare(mealDto.getTimeToPrepare());
        meal.setAppUser(appUser);

        mealRepository.save(meal);
    }

    @Override
    public void delete(Long id) {
        mealRepository.deleteById(id);
    }

    @Override
    public Meal findTopByAppUserOrderByIdDesc(AppUser appUser) {
        return mealRepository.findTopByAppUserOrderByIdDesc(appUser).orElseThrow(NoSuchElementException::new);
    }
}
