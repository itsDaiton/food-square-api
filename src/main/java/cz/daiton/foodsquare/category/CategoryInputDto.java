package cz.daiton.foodsquare.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Object representing a request containing list of categories.")
public class CategoryInputDto {

    @NotNull(message = "Please state whether the recipe contains gluten or not.")
    @Schema(description = "Information, whether the request contains gluten or not.", example = "true")
    private Boolean gluten;

    @NotNull(message = "Please state whether the recipe contains crustaceans or not.")
    @Schema(description = "Information, whether the request contains crustaceans or not.", example = "true")
    private Boolean crustaceans;

    @NotNull(message = "Please state whether the recipe contains eggs or not.")
    @Schema(description = "Information, whether the request contains eggs or not.", example = "true")
    private Boolean eggs;

    @NotNull(message = "Please state whether the recipe contains fish or not.")
    @Schema(description = "Information, whether the request contains fish or not.", example = "true")
    private Boolean fish;

    @NotNull(message = "Please state whether the recipe contains peanuts or not.")
    @Schema(description = "Information, whether the request contains peanuts or not.", example = "true")
    private Boolean peanut;

    @NotNull(message = "Please state whether the recipe contains soy or not.")
    @Schema(description = "Information, whether the request contains soy or not.", example = "true")
    private Boolean soy;

    @NotNull(message = "Please state whether the recipe contains lactose or not.")
    @Schema(description = "Information, whether the request contains lactose or not.", example = "true")
    private Boolean milk;

    @NotNull(message = "Please state whether the recipe contains nuts or not.")
    @Schema(description = "Information, whether the request contains nuts or not.", example = "true")
    private Boolean nuts;

    @NotNull(message = "Please state whether the recipe contains celery or not.")
    @Schema(description = "Information, whether the request contains celery or not.", example = "true")
    private Boolean celery;

    @NotNull(message = "Please state whether the recipe contains mustard or not.")
    @Schema(description = "Information, whether the request contains mustard or not.", example = "true")
    private Boolean mustard;

    @NotNull(message = "Please state whether the recipe contains sesame or not.")
    @Schema(description = "Information, whether the request contains sesame or not.", example = "true")
    private Boolean sesame;

    @NotNull(message = "Please state whether the recipe contains sulphur dioxide or not.")
    @Schema(description = "Information, whether the request contains sulphur or not.", example = "true")
    private Boolean sulphur;

    @NotNull(message = "Please state whether the recipe contains lupin or not.")
    @Schema(description = "Information, whether the request contains lupin or not.", example = "true")
    private Boolean lupin;

    @NotNull(message = "Please state whether the recipe contains molluscs or not.")
    @Schema(description = "Information, whether the request contains molluscs or not.", example = "true")
    private Boolean molluscs;

    @NotNull(message = "Please state whether the recipe contains meat or not.")
    @Schema(description = "Information, whether the request contains meat or not.", example = "true")
    private Boolean meat;
}
