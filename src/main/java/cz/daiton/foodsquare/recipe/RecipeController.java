package cz.daiton.foodsquare.recipe;

import cz.daiton.foodsquare.IO.FileStorageService;
import cz.daiton.foodsquare.appuser.AppUserService;
import cz.daiton.foodsquare.category.Category;
import cz.daiton.foodsquare.exceptions.IncorrectUserException;
import cz.daiton.foodsquare.payload.response.InsertResponse;
import cz.daiton.foodsquare.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping(value = "api/v1/recipes")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@AllArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;
    private final FileStorageService fileStorageService;
    private final AppUserService appUserService;

    @GetMapping(value = "get/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.get(id);
    }

    @GetMapping(value = "/getAll")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @GetMapping(value = "/getCategories/{id}")
    public Set<Category> getCategoriesInRecipe(@PathVariable Long id) {
        return recipeService.getCategoriesInRecipe(id);
    }

    @GetMapping(value = "/getMyFeed/")
    @PreAuthorize("hasRole('USER')")
    public List<Recipe> getMyFeed(HttpServletRequest request) throws IncorrectUserException {
        return recipeService.getAllRecipesOfFollowingAndMine(request);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody RecipeDto recipeDto, HttpServletRequest request) throws Exception {
        Recipe recipe = recipeService.add(recipeDto, request);
        return ResponseEntity
                .ok()
                .body(new InsertResponse(recipe.getId(), "Recipe has been successfully created."));
    }

    @PutMapping(value = "/addImage/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addImage(@PathVariable Long id, @RequestParam("image") MultipartFile file, HttpServletRequest request) throws Exception {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Recipe with id: '" + id + "' has not been found.")
        );
        String message;
        try {
            if (appUserService.checkUser(recipe.getAppUser().getId(), request)) {
                String contentType = file.getContentType();
                Set<String> types = new HashSet<>();
                types.add(MediaType.IMAGE_GIF_VALUE);
                types.add(MediaType.IMAGE_JPEG_VALUE);
                types.add(MediaType.IMAGE_PNG_VALUE);
                if (!types.contains(contentType)) {
                    throw new RuntimeException("Only images are allowed.");
                }
                String pathToImage = fileStorageService.save(file, "recipe", id);
                recipe.setPathToImage(pathToImage);
                recipeRepository.saveAndFlush(recipe);
            }
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(message));
        }
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDto recipeDto, @PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.update(recipeDto, id, request)));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(new MessageResponse(recipeService.delete(id, request)));
    }

    @ExceptionHandler(value =
            {
                    NoSuchElementException.class,
                    HttpMessageNotReadableException.class,
                    IncorrectUserException.class,
                    InvalidDataAccessApiUsageException.class,
                    RuntimeException.class
            })
    public ResponseEntity<?> handleExceptions(Exception e) {
        String message;

        if (e instanceof HttpMessageNotReadableException) {
            message = "Error while parsing JSON. Please enter valid inputs.";
        }
        else if (e instanceof InvalidDataAccessApiUsageException) {
            message = "Wrong format. Try again.";
        }
        else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "This is not valid ID. Please try again.";
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "Wrong request method. Please try again.";
        }
        else {
            message = e.getMessage();
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }
}
