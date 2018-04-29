package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientMO implements Serializable{

    private int ING_ID;

    private String ingredientAkaName;
    private String ingredientCategoryName;
    private List<IngredientImageMO> ingredientPrimaryImage;
    private List<IngredientImageMO> images;
    private List<NutritionCategoryMO> ingredientNutritionCategories;
}
