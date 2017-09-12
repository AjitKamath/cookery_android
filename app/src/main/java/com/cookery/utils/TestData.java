package com.cookery.utils;

import com.cookery.R;
import com.cookery.models.CuisineMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.TasteMO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class TestData {
    public static List<FoodTypeMO> foodTypes;
    public static List<IngredientMO> ingredients;
    public static List<CuisineMO> cuisines;
    public static List<QuantityMO> quantities;
    public static List<TasteMO> tastes;

    static {
        quantities = new ArrayList<>();
        QuantityMO quantity = null;
    }

    static{
        cuisines = new ArrayList<>();
        CuisineMO cuisine = null;

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("INDIAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("CHINESE");
        cuisine.setIS_DEF("Y");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("THAI");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("MEXICAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("SOUTH INDIAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("NORTH INDIAN");
        cuisines.add(cuisine);
    }

    static{
        ingredients = new ArrayList<>();
        IngredientMO ingredient = null;

        ingredient = new IngredientMO();
        ingredient.setING_NAME("MILK");
        ingredient.setING_ID(1);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("SALT");
        ingredient.setING_ID(2);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("PEPPER");
        ingredient.setING_ID(3);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("CHILLY");
        ingredient.setING_ID(4);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("WATER");
        ingredient.setING_ID(5);
        ingredients.add(ingredient);

    }

    static{
        foodTypes = new ArrayList<>();
        FoodTypeMO foodType = null;

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(1);
        foodType.setFOOD_TYP_NAME("BREAKFAST");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(2);
        foodType.setFOOD_TYP_NAME("LUNCH");
        foodType.setIS_DEF("Y");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(3);
        foodType.setFOOD_TYP_NAME("SNACKS");
        foodType.setIS_DEF("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(4);
        foodType.setFOOD_TYP_NAME("DINNER");
        foodType.setIS_DEF("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(5);
        foodType.setFOOD_TYP_NAME("DESSERT");
        foodType.setIS_DEF("");
        foodTypes.add(foodType);
    }
}
