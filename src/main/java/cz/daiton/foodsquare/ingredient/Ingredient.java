package cz.daiton.foodsquare.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.ingredients_in_meal.IngredientsInMeal;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String name;

    @Enumerated(
            EnumType.STRING
    )
    private IngredientCategory category;

    @Column(
            name = "path_to_image"
    )
    private String pathToImage;

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<IngredientsInMeal> ingredientsInMeals = new HashSet<>();

    //TODO: přidat jednotlivé hodnoty(tuky, bílkoviny, sacharidy...)

    //TODO: přidat jednotlivé skutečnosti o ingredienci (isVegan, isGlutenFree...)

    public Ingredient() {

    }

    public Ingredient(Long id, String name, IngredientCategory category, String pathToImage) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.pathToImage = pathToImage;
    }

    public Ingredient(String name, IngredientCategory category, String pathToImage) {
        this.name = name;
        this.category = category;
        this.pathToImage = pathToImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IngredientCategory getCategory() {
        return category;
    }

    public void setCategory(IngredientCategory category) {
        this.category = category;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public Set<IngredientsInMeal> getIngredientsInMeals() {
        return ingredientsInMeals;
    }

    public void setIngredientsInMeals(Set<IngredientsInMeal> ingredientsInMeals) {
        this.ingredientsInMeals = ingredientsInMeals;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", pathToImage='" + pathToImage + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}