package cz.daiton.foodsquare.post.meal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MealDto {

    private final String required = "This field is required.";

    @NotEmpty(message = required)
    private String name;

    @NotEmpty(message = required)
    private String description;

    @NotNull(message = required)
    @Min(value = 1, message = "Preparation time must be at least 1 minute.")
    private Integer timeToPrepare;

    @NotNull(message = required)
    @Min(value = 1, message = "Cooking time must be at least 1 minute.")
    private Integer timeToCook;

    @NotEmpty(message = required)
    private String instructions;

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
}