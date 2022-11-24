package cz.daiton.foodsquare.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "recipe_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Class representing a category used in recipes.")
public class Category {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Name of the category.", example = "GLUTEN_FREE")
    private CategoryType name;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes = new HashSet<>();
}
