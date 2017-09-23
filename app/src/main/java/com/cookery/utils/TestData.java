package com.cookery.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.cookery.R;
import com.cookery.models.CuisineMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class TestData {
    private static List<FoodTypeMO> foodTypes;
    private static List<IngredientMO> ingredients;
    private static List<CuisineMO> cuisines;
    private static List<QuantityMO> quantities;
    private static List<TasteMO> tastes;
    private static List<RecipeMO> recipes;

    public static List<RecipeMO> getRecipesTestData(){
        getIngredientsTestData();

        recipes = new ArrayList<>();
        RecipeMO recipe = null;

        recipe = new RecipeMO();
        recipe.setRCP_NAME("CHICKEN TANDORI");
        recipe.setFOOD_TYP_NAME("DINNER");
        recipe.setFOOD_CSN_NAME("INDIAN");
        recipe.setNAME("Vishal Varhney");
        recipe.setIngredients(ingredients);
        recipes.add(recipe);

        recipe = new RecipeMO();
        recipe.setRCP_NAME("MUSHROOM NOODLES");
        recipe.setFOOD_TYP_NAME("BREAKFAST");
        recipe.setFOOD_CSN_NAME("CHINESE");
        recipe.setIngredients(ingredients);
        recipe.setNAME("Ajit Kamath");
        recipes.add(recipe);

        recipe = new RecipeMO();
        recipe.setRCP_NAME("MASALA DOSA");
        recipe.setFOOD_TYP_NAME("BREAKFAST");
        recipe.setFOOD_CSN_NAME("SOUTH INDIAN");
        recipe.setIngredients(ingredients);
        recipe.setNAME("Ashish Bhargav");
        recipes.add(recipe);

        return recipes;
    }

    public static List<QuantityMO> getQuantitiesTestDate() {
        quantities = new ArrayList<>();
        QuantityMO quantity = null;

        quantity = new QuantityMO();
        quantity.setQTY_ID(1);
        quantity.setIS_DEF("Y");
        quantity.setQTY_NAME("SPOON");
        quantities.add(quantity);

        quantity = new QuantityMO();
        quantity.setQTY_ID(2);
        quantity.setQTY_NAME("CUP");
        quantities.add(quantity);

        quantity = new QuantityMO();
        quantity.setQTY_ID(3);
        quantity.setQTY_NAME("GLASS");
        quantities.add(quantity);

        quantity = new QuantityMO();
        quantity.setQTY_ID(4);
        quantity.setQTY_NAME("TEA SPOON");
        quantities.add(quantity);

        quantity = new QuantityMO();
        quantity.setQTY_ID(5);
        quantity.setQTY_NAME("PINCH");
        quantities.add(quantity);

        return quantities;
    }

    public static List<CuisineMO> getCuisinesTestData(){
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

        return cuisines;
    }

    public static List<IngredientMO> getIngredientsTestData(){
        ingredients = new ArrayList<>();
        IngredientMO ingredient = null;

        ingredient = new IngredientMO();
        ingredient.setING_NAME("MILK");
        ingredient.setING_ID(1);
        ingredient.setQTY(1);
        ingredient.setQTY_NAME("SPOON");
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("SALT");
        ingredient.setING_ID(2);
        ingredient.setQTY(3);
        ingredient.setQTY_NAME("TABLE SPOON");
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("PEPPER");
        ingredient.setING_ID(3);
        ingredient.setQTY(5);
        ingredient.setQTY_NAME("CUP");
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("CHILLY");
        ingredient.setING_ID(4);
        ingredient.setQTY(5);
        ingredient.setQTY_NAME("BOWL");
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setING_NAME("WATER");
        ingredient.setING_ID(5);
        ingredient.setQTY(3);
        ingredient.setQTY_NAME("PINCH");
        ingredients.add(ingredient);

        return ingredients;
    }

    public static List<FoodTypeMO> getFoodTypesTestData(){
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

        return foodTypes;
    }

    public static List<TasteMO> getTastesTestData(){
        TasteMO taste = null;
        List<TasteMO> tastes = new ArrayList<>();

        taste = new TasteMO();
        taste.setTST_ID(1);
        taste.setTST_NAME("SWEET");
        tastes.add(taste);

        taste = new TasteMO();
        taste.setTST_ID(1);
        taste.setTST_NAME("SPICE");
        tastes.add(taste);

        return tastes;
    }

}
