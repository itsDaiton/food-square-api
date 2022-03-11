package cz.daiton.foodsquare.ingredients_in_meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.ingredient.Ingredient;
import cz.daiton.foodsquare.post.meal.Meal;

import javax.persistence.*;

@Entity(name = "ingredients_in_meal")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"meal_id", "ingredient_id"})
})
public class IngredientsInMeal {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Integer amount;
    //TODO: vytvořit možnost zadat gramáž, počet a různé jednotky při výběru

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public IngredientsInMeal() {

    }

    public IngredientsInMeal(Long id, Integer amount, Meal meal, Ingredient ingredient) {
        this.id = id;
        this.amount = amount;
        this.meal = meal;
        this.ingredient = ingredient;
    }

    public IngredientsInMeal(Integer amount, Meal meal, Ingredient ingredient) {
        this.amount = amount;
        this.meal = meal;
        this.ingredient = ingredient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "IngredientsInMeal{" +
                "id=" + id +
                ", amount=" + amount +
                ", meal=" + meal +
                ", ingredient=" + ingredient +
                '}';
    }
}
