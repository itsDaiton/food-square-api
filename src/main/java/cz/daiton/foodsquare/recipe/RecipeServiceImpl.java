package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final RecipeRepository recipeRepository;

    @Override
    public Recipe get(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id'" + id + "' was not found.")
        );
    }

    @Override
    public List<Recipe> getAll() {
        return recipeRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public String add(RecipeDto recipeDto, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = new Recipe();
        AppUser appUser = appUserRepository.findById(recipeDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + recipeDto.getAppUser() + "' does not exist.")
        );

        if (appUserService.checkUser(recipeDto.getAppUser(), request)) {
            recipe.setName(recipeDto.getName());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setInstructions(recipeDto.getInstructions());
            recipe.setTimeToPrepare(recipeDto.getTimeToPrepare());
            recipe.setTimeToCook(recipeDto.getTimeToCook());
            recipe.setAppUser(appUser);
            recipe.setUpdatedAt(LocalDateTime.now());
            recipeRepository.save(recipe);
            return "Recipe has been successfully created.";
        }
        return "There has been a error while trying to add the recipe.";
    }

    @Override
    public String update(RecipeDto recipeDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' has not been found.")
        );

        if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
            recipe.setName(recipeDto.getName());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setInstructions(recipeDto.getInstructions());
            recipe.setTimeToPrepare(recipeDto.getTimeToPrepare());
            recipe.setTimeToCook(recipeDto.getTimeToCook());
            recipe.setUpdatedAt(LocalDateTime.now());
            recipeRepository.save(recipe);
            return "Recipe has been successfully updated.";
        }
        return "There has been a error while trying to update the recipe.";
    }

    @Override
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' does not exist. You cannot delete it.")
        );

        if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
            recipeRepository.deleteById(id);
            return "Recipe has been successfully deleted.";
        }
        return "There has been a error while trying to delete the recipe.";
    }
}
