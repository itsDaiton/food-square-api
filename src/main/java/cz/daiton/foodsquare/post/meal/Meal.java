package cz.daiton.foodsquare.post.meal;

import cz.daiton.foodsquare.ingredients_in_meal.IngredientsInMeal;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String name;

    private String description;

    @Column(
            name = "prep_time"
    )
    private LocalDateTime timeToPrepare;

    @Column(
            name = "cook_time"
    )
    private LocalDateTime timeToCook;

    private String instructions;

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    private Set<IngredientsInMeal> ingredientsInMeals;

    public Meal() {

    }

    public Meal(Long id, String name, String description, LocalDateTime timeToPrepare, LocalDateTime timeToCook, String instructions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeToPrepare = timeToPrepare;
        this.timeToCook = timeToCook;
        this.instructions = instructions;
    }

    public Meal(String name, String description, LocalDateTime timeToPrepare, LocalDateTime timeToCook, String instructions) {
        this.name = name;
        this.description = description;
        this.timeToPrepare = timeToPrepare;
        this.timeToCook = timeToCook;
        this.instructions = instructions;
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

    public LocalDateTime getTimeToPrepare() {
        return timeToPrepare;
    }

    public void setTimeToPrepare(LocalDateTime timeToPrepare) {
        this.timeToPrepare = timeToPrepare;
    }

    public LocalDateTime getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(LocalDateTime timeToCook) {
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
