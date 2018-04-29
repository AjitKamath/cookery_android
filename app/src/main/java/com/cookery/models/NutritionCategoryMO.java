package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutritionCategoryMO implements Serializable {

    private int NUT_CAT_ID;
    private String NUT_CAT_NAME;

    private List<IngredientNutritionMO> ingredientNutritions;
}
