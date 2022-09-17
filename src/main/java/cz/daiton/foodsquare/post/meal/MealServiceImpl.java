package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.post.Post;
import cz.daiton.foodsquare.post.PostRepository;
import cz.daiton.foodsquare.post.PostService;
import cz.daiton.foodsquare.security.IncorrectUserException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final PostService postService;
    private final PostRepository postRepository;

    public MealServiceImpl(MealRepository mealRepository, PostService postService, PostRepository postRepository) {
        this.mealRepository = mealRepository;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @Override
    public Meal get(Long id) {
        return mealRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Meal with id: '" + id + "' does not exist.")
        );
    }

    @Override
    public List<Meal> getAll() {
        return mealRepository.findAllByOrderByIdDesc();
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
    public String update(MealDto mealDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Meal meal = mealRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Meal with id: '" + id + "' does not exist.")
        );
        Post post = postRepository.findByMeal(meal).orElseThrow(
                () -> new NoSuchElementException("Post with meal with id: '" + id + "' has not been found.")
        );

        if (postService.checkUser(post.getAppUser().getId(), request)) {
            meal.setName(mealDto.getName());
            meal.setDescription(mealDto.getDescription());
            meal.setInstructions(mealDto.getInstructions());
            meal.setTimeToCook(mealDto.getTimeToCook());
            meal.setTimeToPrepare(mealDto.getTimeToPrepare());

            mealRepository.save(meal);

            return "Meal has been successfully updated.";
        }
        return "There has been a error while trying to update the meal.";
    }
}
