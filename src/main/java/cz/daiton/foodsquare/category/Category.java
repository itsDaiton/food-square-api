package cz.daiton.foodsquare.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.recipe.Recipe;
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
public class Category {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryType name;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes = new HashSet<>();
}
