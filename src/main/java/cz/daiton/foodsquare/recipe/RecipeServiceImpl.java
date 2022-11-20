package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.IO.FileStorageService;
import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.appuser.AppUserRepository;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.category.CategoryInputDto;
import cz.daiton.foodsquare.category.CategoryRepository;
import cz.daiton.foodsquare.category.CategoryType;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.comment.CommentRepository;
import cz.daiton.foodsquare.comment.CommentService;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.follow.Follow;
import cz.daiton.foodsquare.follow.FollowRepository;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredient;
import cz.daiton.foodsquare.recipe_ingredient.RecipeIngredientRepository;
import cz.daiton.foodsquare.review.Review;
import cz.daiton.foodsquare.review.ReviewRepository;
import cz.daiton.foodsquare.review.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final FileStorageService fileStorageService;

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
    public List<RecipeExtended> getAllExtendedRecipes() {
        List<Recipe> recipes = recipeRepository.findAllByOrderByUpdatedAtDesc();
        List<RecipeExtended> recipeExtendedList = new ArrayList<>();
        for (Recipe r : recipes) {
            RecipeExtended recipeExtended = new RecipeExtended();
            recipeExtended.setId(r.getId());
            recipeExtended.setName(r.getName());
            recipeExtended.setDescription(r.getDescription());
            recipeExtended.setInstructions(r.getInstructions());
            recipeExtended.setTimeToPrepare(r.getTimeToPrepare());
            recipeExtended.setTimeToCook(r.getTimeToCook());
            recipeExtended.setPathToImage(r.getPathToImage());
            recipeExtended.setUpdatedAt(r.getUpdatedAt());
            recipeExtended.setAppUser(r.getAppUser());
            recipeExtended.setCategories(r.getCategories());
            recipeExtended.setMeal(r.getMeal());
            recipeExtended.setAvgRating(reviewService.getAverageRating(r.getId()));
            recipeExtendedList.add((recipeExtended));
        }
        return recipeExtendedList;
    }

    @Override
    public List<Recipe> getAllByUser(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + id + "' was not found.")
        );
        return recipeRepository.findAllByAppUserOrderByUpdatedAtDesc(appUser);
    }

    @Override
    public List<Recipe> getAllRecipesOfFollowingAndMine(HttpServletRequest request) throws IncorrectUserException {
        AppUser me = appUserService.getUserFromCookie(request);

        List<Recipe> myRecipes = recipeRepository.findAllByAppUserOrderByUpdatedAtDesc(me);

        List<Follow> myFollows = followRepository.findAllByFollowerId(me.getId());

        List<Recipe> followRecipes = new ArrayList<>();

        for (Follow f : myFollows) {
            followRecipes.addAll(recipeRepository.findAllByAppUserOrderByUpdatedAtDesc(f.getFollowed()));
        }

        List<Recipe> feed = new ArrayList<>(myRecipes);
        feed.addAll(followRecipes);
        feed.sort(Comparator.comparing(Recipe::getUpdatedAt).reversed());
        return feed;
    }

    @Override
    public Recipe add(RecipeDto recipeDto, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = new Recipe();
        AppUser appUser = appUserRepository.findById(recipeDto.getAppUser()).orElseThrow(
                () -> new NoSuchElementException("User with id: '" + recipeDto.getAppUser() + "' was not found.")
        );
        CategoryInputDto inputs = recipeDto.getInputs();

        if (appUserService.checkUser(recipeDto.getAppUser(), request)) {
            handleInsert(recipeDto, recipe, inputs);
            recipe.setAppUser(appUser);
        }
        return recipeRepository.saveAndFlush(recipe);
    }

    @Override
    public Boolean checkFavorite(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        AppUser me = appUserService.getUserFromCookie(request);

        return me.getFavoriteRecipes().contains(recipe);
    }

    @Override
    public String uploadImage(Long id, MultipartFile file, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        try {
            if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
                String contentType = file.getContentType();
                Set<String> types = new HashSet<>();
                types.add(MediaType.IMAGE_GIF_VALUE);
                types.add(MediaType.IMAGE_JPEG_VALUE);
                types.add(MediaType.IMAGE_PNG_VALUE);
                if (!types.contains(contentType)) {
                    throw new RuntimeException("Only images with format PNG, GIF or JPEG are allowed.");
                }
                String pathToImage = fileStorageService.save(file, "recipe", id);
                recipe.setPathToImage(pathToImage);
                recipeRepository.saveAndFlush(recipe);
                return "Recipe image has been uploaded successfully.";
            }
            return "There has been a error while trying to upload your image.";
        } catch (Exception e) {
            return "Could not recipe image. Error: " + e.getMessage();
        }
    }

    @Override
    public String update(RecipeDto recipeDto, Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        CategoryInputDto inputs = recipeDto.getInputs();

        if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
            handleInsert(recipeDto, recipe, inputs);
            recipeRepository.save(recipe);
            return "Recipe has been successfully updated.";
        }
        return "There has been a error while trying to update the recipe.";
    }

    private void handleInsert(RecipeDto recipeDto, Recipe recipe, CategoryInputDto inputs) {
        Set<Category> categories = handleInputs(inputs);

        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setTimeToPrepare(recipeDto.getTimeToPrepare());
        recipe.setTimeToCook(recipeDto.getTimeToCook());
        recipe.setUpdatedAt(LocalDateTime.now());
        recipe.setCategories(categories);

        String meal = recipeDto.getMeal();
        switch (meal) {
            case "Breakfast":
                recipe.setMeal(RecipeMeal.BREAKFAST);
                break;
            case "Lunch":
                recipe.setMeal(RecipeMeal.LUNCH);
                break;
            case "Dinner":
                recipe.setMeal(RecipeMeal.DINNER);
                break;
            case "Snack":
                recipe.setMeal(RecipeMeal.SNACK);
                break;
            default:
                throw new NoSuchElementException("Please enter a correct meal type.");
        }
    }

    @Override
    @Transactional
    public String delete(Long id, HttpServletRequest request) throws IncorrectUserException {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );

        if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {

            if (!recipe.getIngredientSet().isEmpty()) {
                for (RecipeIngredient ri : recipe.getIngredientSet()) {
                    RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(ri.getId()).orElseThrow(
                            () -> new NoSuchElementException("No record present in the database.")
                    );
                    recipeIngredientRepository.deleteById(recipeIngredient.getId());
                }
                recipe.getIngredientSet().clear();
                recipeIngredientRepository.flush();
            }


            if (!recipe.getComments().isEmpty()) {
                for (Comment c : recipe.getComments()) {
                    commentService.deleteRecursively(c);
                }
                recipe.getComments().clear();
                commentRepository.flush();
            }

            if (!recipe.getReviews().isEmpty()) {
                for (Review r : recipe.getReviews()) {
                    reviewService.deleteRecursively(r);
                }
                recipe.getReviews().clear();
                reviewRepository.flush();
            }

            if (!recipe.getCategories().isEmpty()) {
                List<Category> categories = categoryRepository.findAll();
                for (Category c : categories) {
                    c.getRecipes().remove(recipe);
                }
                recipe.getCategories().clear();
                categoryRepository.saveAllAndFlush(categories);
            }

            if (!recipe.getFavorites().isEmpty()) {
                List<AppUser> users = appUserRepository.findAll();
                for (AppUser a : users) {
                    a.getFavoriteRecipes().remove(recipe);
                }
                recipe.getFavorites().clear();
                appUserRepository.saveAllAndFlush(users);
            }

            recipeRepository.saveAndFlush(recipe);

            if (recipe.getPathToImage() != null) {
                String pathToImage = recipe.getPathToImage();
                fileStorageService.delete(Paths.get(pathToImage));
            }

            recipeRepository.deleteById(id);
            return "Recipe has been successfully deleted.";
        }
        return "There has been a error while trying to delete the recipe.";
    }

    @Override
    public Set<Category> getCategoriesInRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' was not found.")
        );
        return categoryRepository.findAllByRecipes(recipe);
    }

    private Set<Category> handleInputs(CategoryInputDto inputs) {
        Set<Category> categories = new HashSet<>();
        if (!inputs.getGluten()) {
            handleCategory(CategoryType.GLUTEN_FREE, categories);
        }
        if (!inputs.getCrustaceans() && !inputs.getMolluscs()) {
            handleCategory(CategoryType.SHELLFISH_FREE, categories);
        }
        if (!inputs.getEggs()) {
            handleCategory(CategoryType.EGG_FREE, categories);
        }
        if (!inputs.getFish()) {
            handleCategory(CategoryType.FISH_FREE, categories);
        }
        if (!inputs.getSesame()) {
            handleCategory(CategoryType.SESAME_FREE, categories);
        }
        if (!inputs.getNuts()) {
            handleCategory(CategoryType.NUT_FREE, categories);
        }
        if (!inputs.getSoy()) {
            handleCategory(CategoryType.SOY_FREE, categories);
        }
        if (!inputs.getMilk()) {
            handleCategory(CategoryType.LACTOSE_FREE, categories);
        }
        if (!inputs.getPeanut()) {
            handleCategory(CategoryType.PEANUT_FREE, categories);
        }
        if (!inputs.getCelery()) {
            handleCategory(CategoryType.CELERY_FREE, categories);
        }
        if (!inputs.getMustard()) {
            handleCategory(CategoryType.MUSTARD_FREE, categories);
        }
        if (!inputs.getSulphur()) {
            handleCategory(CategoryType.SULPHUR_FREE, categories);
        }
        if (!inputs.getLupin()) {
            handleCategory(CategoryType.LUPIN_FREE, categories);
        }
        if (!inputs.getMeat()) {
            handleCategory(CategoryType.PESCATARIAN, categories);
        }
        if (!inputs.getMeat() && !inputs.getFish() && !inputs.getMolluscs() && !inputs.getCrustaceans()) {
            handleCategory(CategoryType.VEGETARIAN, categories);
        }
        if (!inputs.getMeat() && !inputs.getFish() && !inputs.getMolluscs() && !inputs.getCrustaceans() && !inputs.getEggs() && !inputs.getMilk()) {
            handleCategory(CategoryType.VEGAN, categories);
        }
        return categories;
    }

    private void handleCategory(CategoryType categoryType, Set<Category> categories) {
        Category category = categoryRepository.findByName(categoryType).orElseThrow(
                () -> new NoSuchElementException("This category does not exist.")
        );
        categories.add(category);
    }
}
