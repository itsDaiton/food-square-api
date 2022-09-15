package cz.daiton.foodsquare.post.meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.ingredients_in_meal.IngredientsInMeal;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;
import java.util.HashSet;
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
    private Integer timeToPrepare;

    @Column(
            name = "cook_time"
    )
    private Integer timeToCook;

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

    @ManyToOne
    @JoinColumn(
            name = "app_user_id",
            nullable = false
    )
    private AppUser appUser;

    public Meal() {

    }

    public Meal(Long id, String name, String description, Integer timeToPrepare, Integer timeToCook, String instructions, Set<IngredientsInMeal> ingredientsInMeals, Post post, AppUser appUser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeToPrepare = timeToPrepare;
        this.timeToCook = timeToCook;
        this.instructions = instructions;
        this.ingredientsInMeals = ingredientsInMeals;
        this.post = post;
        this.appUser = appUser;
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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
