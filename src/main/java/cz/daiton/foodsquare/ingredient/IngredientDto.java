package cz.daiton.foodsquare.ingredient;

public class IngredientDto {

    private Long id;

    private String name;

    private IngredientCategory category;

    //TODO: multipartFile pro obrázky

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
}
