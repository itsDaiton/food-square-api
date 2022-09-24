package cz.daiton.foodsquare.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryInputDto {

    @NotNull(message = "Please state whether the recipe contains gluten or not.")
    private Boolean gluten;

    @NotNull(message = "Please state whether the recipe contains crustaceans or not.")
    private Boolean crustaceans;

    @NotNull(message = "Please state whether the recipe contains eggs or not.")
    private Boolean eggs;

    @NotNull(message = "Please state whether the recipe contains fish or not.")
    private Boolean fish;

    @NotNull(message = "Please state whether the recipe contains peanuts or not.")
    private Boolean peanut;

    @NotNull(message = "Please state whether the recipe contains soy or not.")
    private Boolean soy;

    @NotNull(message = "Please state whether the recipe contains lactose or not.")
    private Boolean milk;

    @NotNull(message = "Please state whether the recipe contains nuts or not.")
    private Boolean nuts;

    @NotNull(message = "Please state whether the recipe contains celery or not.")
    private Boolean celery;

    @NotNull(message = "Please state whether the recipe contains mustard or not.")
    private Boolean mustard;

    @NotNull(message = "Please state whether the recipe contains sesame or not.")
    private Boolean sesame;

    @NotNull(message = "Please state whether the recipe contains sulphur dioxide or not.")
    private Boolean sulphur;

    @NotNull(message = "Please state whether the recipe contains lupin or not.")
    private Boolean lupin;

    @NotNull(message = "Please state whether the recipe contains molluscs or not.")
    private Boolean molluscs;

    @NotNull(message = "Please state whether the recipe contains meat or not.")
    private Boolean meat;
}
