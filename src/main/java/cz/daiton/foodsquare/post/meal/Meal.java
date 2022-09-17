package cz.daiton.foodsquare.post.meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.ingredients_in_meal.IngredientsInMeal;
import cz.daiton.foodsquare.post.Post;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "meal")
public class Meal {

    @Transient
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String description;

    @Column(
            name = "prep_time",
            nullable = false
    )
    @NotNull(message = required)
    @Min(value = 1, message = "Preparation time must be at least 1 minute.")
    private Integer timeToPrepare;

    @Column(
            name = "cook_time",
            nullable = false
    )
    @NotNull(message = required)
    @Min(value = 1, message = "Time to cook must be at least 1 minute.")
    private Integer timeToCook;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String instructions;

    @JsonIgnore
    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    private Set<IngredientsInMeal> ingredientsInMeals = new HashSet<>();

    @JsonIgnore
    @OneToOne(
            mappedBy = "meal"
    )
    private Post post;

    public Meal() {

    }

    public Meal(Long id, String name, String description, Integer timeToPrepare, Integer timeToCook, String instructions, Set<IngredientsInMeal> ingredientsInMeals, Post post) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeToPrepare = timeToPrepare;
        this.timeToCook = timeToCook;
        this.instructions = instructions;
        this.ingredientsInMeals = ingredientsInMeals;
        this.post = post;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeToPrepare() {
        return timeToPrepare;
    }

    public void setTimeToPrepare(Integer timeToPrepare) {
        this.timeToPrepare = timeToPrepare;
    }

    public Integer getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(Integer timeToCook) {
        this.timeToCook = timeToCook;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Set<IngredientsInMeal> getIngredientsInMeals() {
        return ingredientsInMeals;
    }

    public void setIngredientsInMeals(Set<IngredientsInMeal> ingredientsInMeals) {
        this.ingredientsInMeals = ingredientsInMeals;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timeToPrepare=" + timeToPrepare +
                ", timeToCook=" + timeToCook +
                ", instructions='" + instructions + '\'' +
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
