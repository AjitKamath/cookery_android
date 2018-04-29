package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientNutritionMO implements Serializable {

    private int ING_NUT_ID;
    private String ING_NUT_VAL;

    private String ingredientNutritionName;
    private String nutritionUOMName;
}
