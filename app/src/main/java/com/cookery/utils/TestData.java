package com.cookery.utils;

import com.cookery.R;
import com.cookery.models.CuisineMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.FoodTypeMO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class TestData {
    public static List<FoodTypeMO> foodTypes;
    public static List<IngredientMO> ingredients;
    public static List<CuisineMO> cuisines;

    static{
        cuisines = new ArrayList<>();
        CuisineMO cuisine = null;

        cuisine = new CuisineMO();
        cuisine.setFood_csn_name("INDIAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFood_csn_name("CHINESE");
        cuisine.setIs_def("Y");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFood_csn_name("THAI");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFood_csn_name("MEXICAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFood_csn_name("SOUTH INDIAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFood_csn_name("NORTH INDIAN");
        cuisines.add(cuisine);
    }

    static{
        ingredients = new ArrayList<>();
        IngredientMO ingredient = null;

        ingredient = new IngredientMO();
        ingredient.setBreed("MILK");
        ingredient.setDrawable(R.drawable.food);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setBreed("BREAD");
        ingredient.setDrawable(R.drawable.food);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setBreed("SALT");
        ingredient.setDrawable(R.drawable.food);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setBreed("PEPPER");
        ingredient.setDrawable(R.drawable.food);
        ingredients.add(ingredient);

        ingredient = new IngredientMO();
        ingredient.setBreed("CHILLY");
        ingredient.setDrawable(R.drawable.food);
        ingredients.add(ingredient);
    }

    static{
        foodTypes = new ArrayList<>();
        FoodTypeMO foodType = null;

        foodType = new FoodTypeMO();
        foodType.setFood_typ_id(1);
        foodType.setFood_typ_name("BREAKFAST");
        foodType.setIs_def("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFood_typ_id(2);
        foodType.setFood_typ_name("LUNCH");
        foodType.setIs_def("Y");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFood_typ_id(3);
        foodType.setFood_typ_name("SNACKS");
        foodType.setIs_def("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFood_typ_id(4);
        foodType.setFood_typ_name("DINNER");
        foodType.setIs_def("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFood_typ_id(5);
        foodType.setFood_typ_name("DESSERT");
        foodType.setIs_def("");
        foodTypes.add(foodType);
    }
}
